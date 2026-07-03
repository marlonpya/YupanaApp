package com.strawtechberry.yupana.feature.accounts.domain

import com.strawtechberry.yupana.feature.accounts.domain.model.Service

/**
 * Streaming service catalog port. RLS already scopes reads to global + the admin's
 * own services; only the admin's own can be created here.
 */
interface ServiceRepository {

    /** Global services plus the admin's own, ordered by name. */
    suspend fun getServices(): Result<List<Service>>

    /** Creates a custom service owned by the admin. */
    suspend fun createService(name: String, color: String?): Result<Service>

    /** Updates one of the admin's own custom services (globals can't be edited; RLS enforces this too). */
    suspend fun updateService(id: String, name: String, color: String?): Result<Service>

    /**
     * Deletes one of the admin's own custom services. Fails with
     * [com.strawtechberry.yupana.feature.accounts.domain.model.AccountsError.ServiceInUse] if an
     * account still references it (`account_service_id_fkey` is `ON DELETE RESTRICT`).
     */
    suspend fun deleteService(id: String): Result<Unit>
}
