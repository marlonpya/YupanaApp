package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Searches clients by name (case-insensitive substring match), ordered by name. */
class SearchClientsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(query: String): Result<List<Client>> =
        repository.searchClients(query.trim())
}
