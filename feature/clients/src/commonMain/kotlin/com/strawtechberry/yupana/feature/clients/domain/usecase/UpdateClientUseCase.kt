package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Updates an existing client. */
class UpdateClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(id: String, name: String, contact: String?, notes: String?): Result<Client> =
        repository.updateClient(id, name.trim(), contact?.trim()?.ifBlank { null }, notes?.trim()?.ifBlank { null })
}
