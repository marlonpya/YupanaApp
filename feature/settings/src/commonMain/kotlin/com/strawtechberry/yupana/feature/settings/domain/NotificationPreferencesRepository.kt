package com.strawtechberry.yupana.feature.settings.domain

import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences

/** Port over the `user_preferences` table (one row per admin, RLS-scoped). */
interface NotificationPreferencesRepository {

    /** Returns the admin's saved preferences, or [NotificationPreferences] defaults if none saved yet. */
    suspend fun getPreferences(): Result<NotificationPreferences>

    /** Upserts the admin's preferences (one row per `owner_id`). */
    suspend fun updatePreferences(preferences: NotificationPreferences): Result<Unit>
}
