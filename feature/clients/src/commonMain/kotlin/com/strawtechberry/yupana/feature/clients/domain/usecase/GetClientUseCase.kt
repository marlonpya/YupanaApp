package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Loads a single client by id (used by the form in edit mode). */
class GetClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(id: String): Result<Client> = repository.getClient(id)
}
