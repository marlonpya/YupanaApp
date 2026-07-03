package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentMoveContext

/** Loads the current profile/account/service of an assignment, for the "Mover integrante" screen. */
class GetAssignmentMoveContextUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignmentId: String): Result<AssignmentMoveContext> =
        repository.getAssignmentMoveContext(assignmentId)
}
