package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment

/** Links a free profile to a client with a price and due date. */
class AssignProfileToClientUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(
        profileId: String,
        clientId: String,
        priceCharged: Double,
        dueDate: String,
    ): Result<Assignment> = repository.assignProfile(profileId, clientId, priceCharged, dueDate)
}
