package com.strawtechberry.yupana.feature.settings.domain.usecase

import com.strawtechberry.yupana.feature.settings.domain.NotificationPreferencesRepository
import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences

class GetNotificationPreferencesUseCase(private val repository: NotificationPreferencesRepository) {
    suspend operator fun invoke(): Result<NotificationPreferences> = repository.getPreferences()
}
