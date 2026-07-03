package com.strawtechberry.yupana.feature.settings.ui.root

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

data class SettingsRootUiState(
    val showLogoutConfirm: Boolean = false,
) : UiState

sealed interface SettingsRootIntent : UiIntent {
    data object MyAccountClicked : SettingsRootIntent
    data object NotificationPreferencesClicked : SettingsRootIntent
    data object ServiceCatalogClicked : SettingsRootIntent
    data object LogoutClicked : SettingsRootIntent
    data object DismissLogoutConfirm : SettingsRootIntent
    data object ConfirmLogout : SettingsRootIntent
}

sealed interface SettingsRootEvent : UiEvent {
    data object NavigateToMyAccount : SettingsRootEvent
    data object NavigateToNotificationPreferences : SettingsRootEvent
    data object NavigateToServiceCatalog : SettingsRootEvent
    data object LoggedOut : SettingsRootEvent
}
