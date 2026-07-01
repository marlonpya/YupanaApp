package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentException
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CancellationException

/**
 * Implementation of [AssignmentRepository] over supabase-kt Postgrest. `assignment`
 * has a unique index (one active assignment per profile) enforced at the database
 * level — see [mapAssignmentError] for how that violation is surfaced.
 */
class DefaultAssignmentRepository(private val postgrest: Postgrest) : AssignmentRepository {

    override suspend fun assignProfile(
        profileId: String,
        clientId: String,
        priceCharged: Double,
        dueDate: String,
    ): Result<Assignment> =
        execute {
            postgrest.from("assignment")
                .insert(
                    AssignmentInsertDto(
                        profileId = profileId,
                        clientId = clientId,
                        priceCharged = priceCharged,
                        dueDate = dueDate,
                    ),
                ) { select() }
                .decodeSingle<AssignmentDto>()
                .toDomain()
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AssignmentException(mapAssignmentError(t)))
    }
}
