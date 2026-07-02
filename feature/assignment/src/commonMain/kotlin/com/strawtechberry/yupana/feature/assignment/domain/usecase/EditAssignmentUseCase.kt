package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository

class EditAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String, priceCharged: Double, dueDate: String): Result<Unit> =
        repository.updateAssignment(assignmentId, priceCharged, dueDate)
}
