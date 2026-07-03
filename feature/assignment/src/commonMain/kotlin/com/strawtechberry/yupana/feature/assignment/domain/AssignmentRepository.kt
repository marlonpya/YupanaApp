package com.strawtechberry.yupana.feature.assignment.domain

import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentMoveContext
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

/**
 * Assignment port. The implementation ([data.DefaultAssignmentRepository]) uses
 * supabase-kt Postgrest; domain and presentation don't know about the SDK. Errors
 * travel as `Result.failure(AssignmentException(...))`.
 */
interface AssignmentRepository {

    /** Links a free profile to a client. Fails with [com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentError.ProfileAlreadyAssigned] if the profile got taken first. */
    suspend fun assignProfile(
        profileId: String,
        clientId: String,
        priceCharged: Double,
        dueDate: String,
    ): Result<Assignment>

    /** All active assignments with their urgency (`upcoming_expirations` view), soonest first. */
    suspend fun getUpcomingExpirations(): Result<List<UpcomingExpiration>>

    suspend fun getUpcomingExpiration(assignmentId: String): Result<UpcomingExpiration>

    /** Pushes `due_date` one calendar month past [currentDueDate] and resets `reminder_sent`. */
    suspend fun renewAssignment(assignmentId: String, currentDueDate: String): Result<Unit>

    suspend fun updateAssignment(assignmentId: String, priceCharged: Double, dueDate: String): Result<Unit>

    /** Soft-cancels the assignment (`status = 'cancelled'`); the profile becomes free again. */
    suspend fun liberateAssignment(assignmentId: String): Result<Unit>

    /** Where the assignment's profile currently lives — used to find same-service destination accounts. */
    suspend fun getAssignmentMoveContext(assignmentId: String): Result<AssignmentMoveContext>

    /** Moves the assignment to another profile, keeping price/due date. Same unique-index race as [assignProfile]. */
    suspend fun moveAssignment(assignmentId: String, newProfileId: String): Result<Unit>
}
