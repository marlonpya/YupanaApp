package com.strawtechberry.yupana.feature.settings.ui.notifications

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences
import com.strawtechberry.yupana.feature.settings.domain.usecase.GetNotificationPreferencesUseCase
import com.strawtechberry.yupana.feature.settings.domain.usecase.UpdateNotificationPreferencesUseCase
import kotlinx.coroutines.launch

class NotificationPreferencesViewModel(
    private val getPreferences: GetNotificationPreferencesUseCase,
    private val updatePreferences: UpdateNotificationPreferencesUseCase,
) : MviViewModel<NotificationPreferencesUiState, NotificationPreferencesIntent, NotificationPreferencesEvent>(
    NotificationPreferencesUiState(),
) {

    init { load() }

    override fun onIntent(intent: NotificationPreferencesIntent) {
        when (intent) {
            NotificationPreferencesIntent.Retry -> load()
            is NotificationPreferencesIntent.EnabledChanged -> setState { copy(enabled = intent.enabled, saved = false) }
            is NotificationPreferencesIntent.DayToggled -> setState {
                val days = if (daysBefore.contains(intent.day)) daysBefore - intent.day else daysBefore + intent.day
                copy(daysBefore = days, saved = false)
            }
            NotificationPreferencesIntent.HourIncremented ->
                setState { copy(reminderHour = (reminderHour + 1).coerceAtMost(23), saved = false) }
            NotificationPreferencesIntent.HourDecremented ->
                setState { copy(reminderHour = (reminderHour - 1).coerceAtLeast(0), saved = false) }
            NotificationPreferencesIntent.SaveClicked -> save()
            NotificationPreferencesIntent.BackClicked -> sendEvent(NotificationPreferencesEvent.NavigateBack)
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getPreferences().fold(
                onSuccess = { prefs ->
                    setState {
                        copy(
                            isLoading = false,
                            enabled = prefs.enabled,
                            daysBefore = prefs.daysBefore,
                            reminderHour = prefs.reminderHour,
                        )
                    }
                },
                onFailure = { setState { copy(isLoading = false, error = "No se pudieron cargar las preferencias") } },
            )
        }
    }

    private fun save() {
        viewModelScope.launch {
            setState { copy(isSaving = true, error = null) }
            val preferences = NotificationPreferences(
                enabled = currentState.enabled,
                daysBefore = currentState.daysBefore,
                reminderHour = currentState.reminderHour,
            )
            updatePreferences(preferences).fold(
                onSuccess = { setState { copy(isSaving = false, saved = true) } },
                onFailure = { setState { copy(isSaving = false, error = "No se pudo guardar. Inténtalo de nuevo") } },
            )
        }
    }
}
