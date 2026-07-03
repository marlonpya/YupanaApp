package com.strawtechberry.yupana.feature.settings.ui.notifications

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

data class NotificationPreferencesUiState(
    val isLoading: Boolean = true,
    val enabled: Boolean = true,
    val daysBefore: Set<Int> = emptySet(),
    val reminderHour: Int = 9,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
) : UiState

sealed interface NotificationPreferencesIntent : UiIntent {
    data object Retry : NotificationPreferencesIntent
    data class EnabledChanged(val enabled: Boolean) : NotificationPreferencesIntent
    data class DayToggled(val day: Int) : NotificationPreferencesIntent
    data object HourIncremented : NotificationPreferencesIntent
    data object HourDecremented : NotificationPreferencesIntent
    data object SaveClicked : NotificationPreferencesIntent
    data object BackClicked : NotificationPreferencesIntent
}

sealed interface NotificationPreferencesEvent : UiEvent {
    data object NavigateBack : NotificationPreferencesEvent
}
