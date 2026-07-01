package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsException
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import com.strawtechberry.yupana.feature.accounts.domain.model.ProfileOccupant
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException

/**
 * Implementation of [AccountRepository] over supabase-kt Postgrest. Profiles have no
 * standalone CRUD screen in this phase, so their creation/reads live here as a side
 * effect of account operations (see [createAccount]/[updateAccount]).
 */
class DefaultAccountRepository(private val postgrest: Postgrest) : AccountRepository {

    override suspend fun getAccounts(): Result<List<Account>> =
        execute {
            postgrest.from("account").select {
                order(column = "alias", order = Order.ASCENDING)
            }.decodeList<AccountDto>().map { it.toDomain() }
        }

    override suspend fun getAccount(id: String): Result<Account> =
        execute {
            postgrest.from("account").select {
                filter { eq("id", id) }
            }.decodeSingle<AccountDto>().toDomain()
        }

    override suspend fun createAccount(
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account> =
        execute {
            val account = postgrest.from("account")
                .insert(
                    AccountUpsertDto(
                        serviceId = serviceId,
                        alias = alias,
                        monthlyCost = monthlyCost,
                        numProfiles = numProfiles,
                        billingDay = billingDay,
                        notes = notes,
                    ),
                ) { select() }
                .decodeSingle<AccountDto>()

            val profileRows = (1..numProfiles).map { ProfileInsertDto(accountId = account.id, label = "Perfil $it") }
            if (profileRows.isNotEmpty()) {
                postgrest.from("profile").insert(profileRows)
            }
            account.toDomain()
        }

    override suspend fun updateAccount(
        id: String,
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account> =
        execute {
            val updated = postgrest.from("account")
                .update(
                    AccountUpsertDto(
                        serviceId = serviceId,
                        alias = alias,
                        monthlyCost = monthlyCost,
                        numProfiles = numProfiles,
                        billingDay = billingDay,
                        notes = notes,
                    ),
                ) {
                    select()
                    filter { eq("id", id) }
                }
                .decodeSingle<AccountDto>()

            // Grow profile slots if numProfiles increased; never shrink (no destructive
            // deletes here — a decreased count could orphan an occupied profile).
            val existing = postgrest.from("profile").select {
                filter { eq("account_id", id) }
            }.decodeList<ProfileDto>()
            if (numProfiles > existing.size) {
                val newRows = (existing.size + 1..numProfiles).map { ProfileInsertDto(accountId = id, label = "Perfil $it") }
                postgrest.from("profile").insert(newRows)
            }
            updated.toDomain()
        }

    override suspend fun getOccupiedProfileCounts(): Result<Map<String, Int>> =
        execute {
            val profiles = postgrest.from("profile")
                .select(Columns.list("id", "account_id"))
                .decodeList<ProfileAccountRefDto>()
            if (profiles.isEmpty()) return@execute emptyMap()

            val activeProfileIds = postgrest.from("assignment")
                .select(Columns.list("profile_id")) { filter { eq("status", "active") } }
                .decodeList<AssignmentProfileIdDto>()
                .map { it.profileId }
                .toSet()

            profiles.filter { it.id in activeProfileIds }.groupingBy { it.accountId }.eachCount()
        }

    override suspend fun getProfilesForAccount(accountId: String): Result<List<Profile>> =
        execute {
            val profiles = postgrest.from("profile").select {
                filter { eq("account_id", accountId) }
                order(column = "label", order = Order.ASCENDING)
            }.decodeList<ProfileDto>()
            if (profiles.isEmpty()) return@execute emptyList()

            val ids = profiles.map { it.id }
            val occupantsByProfileId = postgrest.from("assignment")
                .select(Columns.raw("*, client(name)")) {
                    filter {
                        eq("status", "active")
                        isIn("profile_id", ids)
                    }
                }
                .decodeList<AssignmentOccupantDto>()
                .associateBy { it.profileId }

            profiles.map { profile ->
                val occupant = occupantsByProfileId[profile.id]?.let {
                    ProfileOccupant(
                        clientName = it.client?.name ?: "Cliente",
                        priceCharged = it.priceCharged,
                        dueDate = it.dueDate,
                    )
                }
                profile.toDomain(occupant)
            }
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AccountsException(mapAccountsError(t)))
    }
}
