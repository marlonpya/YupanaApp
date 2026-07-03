package com.strawtechberry.yupana.feature.settings.di

import com.strawtechberry.yupana.feature.settings.data.DefaultNotificationPreferencesRepository
import com.strawtechberry.yupana.feature.settings.domain.NotificationPreferencesRepository
import com.strawtechberry.yupana.feature.settings.domain.usecase.GetNotificationPreferencesUseCase
import com.strawtechberry.yupana.feature.settings.domain.usecase.UpdateNotificationPreferencesUseCase
import com.strawtechberry.yupana.feature.settings.ui.account.MyAccountViewModel
import com.strawtechberry.yupana.feature.settings.ui.notifications.NotificationPreferencesViewModel
import com.strawtechberry.yupana.feature.settings.ui.root.SettingsRootViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Settings Koin module. Reuses `feature.auth`'s `AuthRepository`-backed use cases
 * (change password, current user, sign out) for "Mi cuenta"; this module must be
 * loaded alongside `authModule` and `supabaseModule`.
 */
val settingsModule = module {
    single<NotificationPreferencesRepository> { DefaultNotificationPreferencesRepository(get()) }

    factoryOf(::GetNotificationPreferencesUseCase)
    factoryOf(::UpdateNotificationPreferencesUseCase)

    viewModelOf(::SettingsRootViewModel)
    viewModelOf(::NotificationPreferencesViewModel)
    viewModelOf(::MyAccountViewModel)
}
