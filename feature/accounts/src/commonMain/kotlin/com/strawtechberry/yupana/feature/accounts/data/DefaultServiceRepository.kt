package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsException
import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException

/**
 * Implementation of [ServiceRepository] over supabase-kt Postgrest. `streaming_service`
 * RLS scopes SELECT to global + own automatically, but INSERT has no `owner_id`
 * default (unlike account/profile), so [Auth] is needed to fill it explicitly.
 */
class DefaultServiceRepository(
    private val postgrest: Postgrest,
    private val auth: Auth,
) : ServiceRepository {

    override suspend fun getServices(): Result<List<Service>> =
        execute {
            postgrest.from("streaming_service").select {
                order(column = "name", order = Order.ASCENDING)
            }.decodeList<ServiceDto>().map { it.toDomain() }
        }

    override suspend fun createService(name: String, color: String?): Result<Service> =
        execute {
            val ownerId = auth.currentSessionOrNull()?.user?.id
                ?: error("No hay sesión activa")
            postgrest.from("streaming_service")
                .insert(ServiceInsertDto(name = name, color = color, ownerId = ownerId)) { select() }
                .decodeSingle<ServiceDto>()
                .toDomain()
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AccountsException(mapAccountsError(t)))
    }
}
