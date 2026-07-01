package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository

/** "Liberar perfil": soft-cancels the assignment so the profile becomes free again. */
class LiberateAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String): Result<Unit> = repository.liberateAssignment(assignmentId)
}
