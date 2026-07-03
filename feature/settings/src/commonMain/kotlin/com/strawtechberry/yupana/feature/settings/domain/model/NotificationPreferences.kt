package com.strawtechberry.yupana.feature.settings.domain.model

/**
 * Notification preferences for the reminder push (consumed later by Group 11's
 * server-side cron/Edge Function, hence living in Supabase rather than local storage).
 */
data class NotificationPreferences(
    val enabled: Boolean = true,
    val daysBefore: Set<Int> = setOf(1, 3, 7),
    val reminderHour: Int = 9,
) {
    companion object {
        val DAYS_BEFORE_OPTIONS = listOf(1, 2, 3, 5, 7)
    }
}
