package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository

/** Moves an assignment to another (free) profile, keeping price and due date. */
class MoveAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String, newProfileId: String): Result<Unit> =
        repository.moveAssignment(assignmentId, newProfileId)
}
