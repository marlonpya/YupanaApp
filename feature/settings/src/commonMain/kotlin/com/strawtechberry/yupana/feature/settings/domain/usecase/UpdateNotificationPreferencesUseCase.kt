package com.strawtechberry.yupana.feature.settings.domain.usecase

import com.strawtechberry.yupana.feature.settings.domain.NotificationPreferencesRepository
import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences

class UpdateNotificationPreferencesUseCase(private val repository: NotificationPreferencesRepository) {
    suspend operator fun invoke(preferences: NotificationPreferences): Result<Unit> =
        repository.updatePreferences(preferences)
}
