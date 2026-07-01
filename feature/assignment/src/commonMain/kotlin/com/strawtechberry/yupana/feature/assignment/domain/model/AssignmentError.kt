package com.strawtechberry.yupana.feature.assignment.domain.model

/** Assignment operation errors in domain language (translatable to UI messages). */
sealed interface AssignmentError {
    data object NoConnection : AssignmentError

    /** The `assignment` unique index rejected the insert: the profile got taken first. */
    data object ProfileAlreadyAssigned : AssignmentError

    data class Unknown(val detail: String?) : AssignmentError
}

/** Exception that carries an [AssignmentError] through `Result.failure`. */
class AssignmentException(val error: AssignmentError) : Exception()
