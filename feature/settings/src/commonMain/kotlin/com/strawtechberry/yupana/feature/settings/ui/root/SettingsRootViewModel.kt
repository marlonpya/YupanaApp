package com.strawtechberry.yupana.feature.settings.ui.root

import com.strawtechberry.yupana.core.mvi.MviViewModel

class SettingsRootViewModel : MviViewModel<SettingsRootUiState, SettingsRootIntent, SettingsRootEvent>(SettingsRootUiState()) {

    override fun onIntent(intent: SettingsRootIntent) {
        when (intent) {
            SettingsRootIntent.MyAccountClicked -> sendEvent(SettingsRootEvent.NavigateToMyAccount)
            SettingsRootIntent.NotificationPreferencesClicked -> sendEvent(SettingsRootEvent.NavigateToNotificationPreferences)
            SettingsRootIntent.ServiceCatalogClicked -> sendEvent(SettingsRootEvent.NavigateToServiceCatalog)
            SettingsRootIntent.LogoutClicked -> setState { copy(showLogoutConfirm = true) }
            SettingsRootIntent.DismissLogoutConfirm -> setState { copy(showLogoutConfirm = false) }
            SettingsRootIntent.ConfirmLogout -> {
                setState { copy(showLogoutConfirm = false) }
                sendEvent(SettingsRootEvent.LoggedOut)
            }
        }
    }
}
