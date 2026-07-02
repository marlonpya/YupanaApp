package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

class GetUpcomingExpirationUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String): Result<UpcomingExpiration> =
        repository.getUpcomingExpiration(assignmentId)
}
