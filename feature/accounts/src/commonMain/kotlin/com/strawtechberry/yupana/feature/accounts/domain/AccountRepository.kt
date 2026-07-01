package com.strawtechberry.yupana.feature.accounts.domain

import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile

/**
 * Account + profile port. Profiles have no standalone CRUD screen in this phase, so
 * their operations live here as a side effect of account operations.
 */
interface AccountRepository {

    /** All of the admin's accounts, ordered by alias. */
    suspend fun getAccounts(): Result<List<Account>>

    /** A single account by id. */
    suspend fun getAccount(id: String): Result<Account>

    /** Creates an account and auto-creates [numProfiles] blank profile slots for it. */
    suspend fun createAccount(
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account>

    /**
     * Updates an account. If [numProfiles] grew, creates the missing profile slots;
     * if it shrank, existing profiles are left untouched (no destructive deletes here).
     */
    suspend fun updateAccount(
        id: String,
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account>

    /** Occupied profile count per account id, for the grouped list's summary/bars. */
    suspend fun getOccupiedProfileCounts(): Result<Map<String, Int>>

    /** Profiles of one account, each with its occupant if assigned, for the detail screen. */
    suspend fun getProfilesForAccount(accountId: String): Result<List<Profile>>
}
