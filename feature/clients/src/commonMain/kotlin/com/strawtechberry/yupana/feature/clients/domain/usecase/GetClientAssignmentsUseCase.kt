package com.strawtechberry.yupana.feature.clients.domain.usecase

import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.model.ClientAssignment

/** Loads a client's assignments (active and cancelled), for the client detail screen. */
class GetClientAssignmentsUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(clientId: String): Result<List<ClientAssignment>> =
        repository.getClientAssignments(clientId)
}
