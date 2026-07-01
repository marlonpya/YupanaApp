package com.strawtechberry.yupana.feature.clients.domain

import com.strawtechberry.yupana.feature.clients.domain.model.Client

/**
 * Client port. The implementation ([data.DefaultClientRepository]) uses supabase-kt
 * Postgrest; domain and presentation don't know about the SDK. Errors travel as
 * `Result.failure(ClientException(...))`.
 */
interface ClientRepository {

    /** All clients of the authenticated admin, ordered by name. */
    suspend fun getClients(): Result<List<Client>>

    /** Clients whose name matches [query] (case-insensitive substring), ordered by name. */
    suspend fun searchClients(query: String): Result<List<Client>>

    /** A single client by id. */
    suspend fun getClient(id: String): Result<Client>

    /** Creates a new client. */
    suspend fun createClient(name: String, contact: String?, notes: String?): Result<Client>

    /** Updates an existing client. */
    suspend fun updateClient(id: String, name: String, contact: String?, notes: String?): Result<Client>
}
