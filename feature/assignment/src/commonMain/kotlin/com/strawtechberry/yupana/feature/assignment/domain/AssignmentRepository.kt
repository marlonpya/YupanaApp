package com.strawtechberry.yupana.feature.assignment.domain

import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment

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
}
