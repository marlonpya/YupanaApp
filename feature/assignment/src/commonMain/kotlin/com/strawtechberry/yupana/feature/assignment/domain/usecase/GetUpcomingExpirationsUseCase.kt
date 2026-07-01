package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

class GetUpcomingExpirationsUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(): Result<List<UpcomingExpiration>> = repository.getUpcomingExpirations()
}
