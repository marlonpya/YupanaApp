package com.strawtechberry.yupana.feature.settings.data

import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationPreferencesDto(
    val enabled: Boolean = true,
    @SerialName("days_before") val daysBefore: List<Int> = listOf(1, 3, 7),
    @SerialName("reminder_hour") val reminderHour: Int = 9,
) {
    fun toDomain(): NotificationPreferences = NotificationPreferences(
        enabled = enabled,
        daysBefore = daysBefore.toSet(),
        reminderHour = reminderHour,
    )
}

/** Upsert payload: `owner_id` is left to the column default (`auth.uid()`), same as other tables. */
@Serializable
data class NotificationPreferencesUpsertDto(
    val enabled: Boolean,
    @SerialName("days_before") val daysBefore: List<Int>,
    @SerialName("reminder_hour") val reminderHour: Int,
)

fun NotificationPreferences.toUpsertDto(): NotificationPreferencesUpsertDto = NotificationPreferencesUpsertDto(
    enabled = enabled,
    daysBefore = daysBefore.sorted(),
    reminderHour = reminderHour,
)
