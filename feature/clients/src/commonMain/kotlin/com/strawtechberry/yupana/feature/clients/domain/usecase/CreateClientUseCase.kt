package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Creates a new client for the authenticated admin. */
class CreateClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(name: String, contact: String?, notes: String?): Result<Client> =
        repository.createClient(name.trim(), contact?.trim()?.ifBlank { null }, notes?.trim()?.ifBlank { null })
}
