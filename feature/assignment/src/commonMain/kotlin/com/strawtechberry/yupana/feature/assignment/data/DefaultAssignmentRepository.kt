package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentException
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentMoveContext
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
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

    override suspend fun getUpcomingExpirations(): Result<List<UpcomingExpiration>> =
        execute {
            postgrest.from("upcoming_expirations")
                .select { order("days_left", Order.ASCENDING) }
                .decodeList<UpcomingExpirationDto>()
                .map { it.toDomain() }
        }

    override suspend fun getUpcomingExpiration(assignmentId: String): Result<UpcomingExpiration> =
        execute {
            postgrest.from("upcoming_expirations")
                .select { filter { eq("assignment_id", assignmentId) } }
                .decodeSingle<UpcomingExpirationDto>()
                .toDomain()
        }

    override suspend fun renewAssignment(assignmentId: String, currentDueDate: String): Result<Unit> =
        execute {
            postgrest.from("assignment").update(
                AssignmentRenewDto(dueDate = addOneMonthIso(currentDueDate), reminderSent = false),
            ) { filter { eq("id", assignmentId) } }
            Unit
        }

    override suspend fun updateAssignment(assignmentId: String, priceCharged: Double, dueDate: String): Result<Unit> =
        execute {
            postgrest.from("assignment").update(
                AssignmentEditDto(priceCharged = priceCharged, dueDate = dueDate),
            ) { filter { eq("id", assignmentId) } }
            Unit
        }

    override suspend fun liberateAssignment(assignmentId: String): Result<Unit> =
        execute {
            postgrest.from("assignment").update(
                AssignmentStatusDto(status = "cancelled"),
            ) { filter { eq("id", assignmentId) } }
            Unit
        }

    override suspend fun getAssignmentMoveContext(assignmentId: String): Result<AssignmentMoveContext> =
        execute {
            postgrest.from("assignment")
                .select(Columns.raw("profile_id, profile(account_id, account(service_id))")) {
                    filter { eq("id", assignmentId) }
                }
                .decodeSingle<AssignmentMoveContextDto>()
                .toDomain()
        }

    override suspend fun moveAssignment(assignmentId: String, newProfileId: String): Result<Unit> =
        execute {
            postgrest.from("assignment").update(
                AssignmentMoveDto(profileId = newProfileId),
            ) { filter { eq("id", assignmentId) } }
            Unit
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AssignmentException(mapAssignmentError(t)))
    }
}
