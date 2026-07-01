package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Lists all clients of the authenticated admin, ordered by name. */
class GetClientsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(): Result<List<Client>> = repository.getClients()
}
