package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository

/** "Marcar como cobrado": pushes the due date one calendar month forward. */
class RenewAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String, currentDueDate: String): Result<Unit> =
        repository.renewAssignment(assignmentId, currentDueDate)
}
