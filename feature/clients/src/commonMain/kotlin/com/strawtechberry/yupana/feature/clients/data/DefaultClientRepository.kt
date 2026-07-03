package com.strawtechberry.yupana.feature.clients.data

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client
import com.strawtechberry.yupana.feature.clients.domain.model.ClientAssignment
import com.strawtechberry.yupana.feature.clients.domain.model.ClientException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException

/**
 * Implementation of [ClientRepository] over supabase-kt Postgrest. This repository
 * only translates between its API and the domain, and maps errors to [ClientException].
 *
 * postgrest-kt 3.6.0 (pinned in this project) requires filters to go through an
 * explicit `filter { }` block, and insert/update need an explicit `select()` call in
 * their request block to get the affected row back (default `Returning` is Minimal) —
 * both confirmed against the actual 3.6.0 source, not the (newer) public docs.
 */
class DefaultClientRepository(private val postgrest: Postgrest) : ClientRepository {

    override suspend fun getClients(): Result<List<Client>> =
        execute {
            postgrest.from("client").select {
                order(column = "name", order = Order.ASCENDING)
            }.decodeList<ClientDto>().map { it.toDomain() }
        }

    override suspend fun searchClients(query: String): Result<List<Client>> =
        execute {
            postgrest.from("client").select {
                filter { ilike("name", "%$query%") }
                order(column = "name", order = Order.ASCENDING)
            }.decodeList<ClientDto>().map { it.toDomain() }
        }

    override suspend fun getClient(id: String): Result<Client> =
        execute {
            postgrest.from("client").select {
                filter { eq("id", id) }
            }.decodeSingle<ClientDto>().toDomain()
        }

    override suspend fun getClientAssignments(clientId: String): Result<List<ClientAssignment>> =
        execute {
            postgrest.from("assignment")
                .select(Columns.raw("*, profile(label, account(alias, streaming_service(name)))")) {
                    filter { eq("client_id", clientId) }
                    order(column = "due_date", order = Order.ASCENDING)
                }
                .decodeList<ClientAssignmentDto>()
                .map { it.toDomain() }
        }

    override suspend fun createClient(name: String, contact: String?, notes: String?): Result<Client> =
        execute {
            postgrest.from("client")
                .insert(ClientUpsertDto(name = name, contact = contact, notes = notes)) {
                    select()
                }
                .decodeSingle<ClientDto>()
                .toDomain()
        }

    override suspend fun updateClient(id: String, name: String, contact: String?, notes: String?): Result<Client> =
        execute {
            postgrest.from("client")
                .update(ClientUpsertDto(name = name, contact = contact, notes = notes)) {
                    select()
                    filter { eq("id", id) }
                }
                .decodeSingle<ClientDto>()
                .toDomain()
        }

    /** Executes the operation and wraps the result; propagates cancellation, maps the rest. */
    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(ClientException(mapClientError(t)))
    }
}
