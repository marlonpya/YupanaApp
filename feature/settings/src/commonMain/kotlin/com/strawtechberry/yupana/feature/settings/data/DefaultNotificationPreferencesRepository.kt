package com.strawtechberry.yupana.feature.settings.data

import com.strawtechberry.yupana.feature.settings.domain.NotificationPreferencesRepository
import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CancellationException

/**
 * Implementation of [NotificationPreferencesRepository] over supabase-kt Postgrest.
 * `user_preferences` has one row per admin (`owner_id` unique, RLS-scoped); a missing
 * row (new admin, never saved) is treated as [NotificationPreferences] defaults rather
 * than an error.
 */
class DefaultNotificationPreferencesRepository(private val postgrest: Postgrest) : NotificationPreferencesRepository {

    override suspend fun getPreferences(): Result<NotificationPreferences> =
        execute {
            postgrest.from("user_preferences")
                .select()
                .decodeList<NotificationPreferencesDto>()
                .firstOrNull()
                ?.toDomain()
                ?: NotificationPreferences()
        }

    override suspend fun updatePreferences(preferences: NotificationPreferences): Result<Unit> =
        execute {
            postgrest.from("user_preferences")
                .upsert(preferences.toUpsertDto()) { onConflict = "owner_id" }
            Unit
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(t)
    }
}
