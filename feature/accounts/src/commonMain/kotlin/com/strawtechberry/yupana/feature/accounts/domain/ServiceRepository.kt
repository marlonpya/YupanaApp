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
}
