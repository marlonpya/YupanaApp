package com.strawtechberry.yupana.feature.settings.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/** "Ajustes" root: navigates to Mi cuenta / Preferencias de notificación / Catálogo, and logs out. */
@Composable
fun SettingsRootRoute(
    onOpenMyAccount: () -> Unit,
    onOpenNotificationPreferences: () -> Unit,
    onOpenServiceCatalog: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: SettingsRootViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SettingsRootEvent.NavigateToMyAccount -> onOpenMyAccount()
                SettingsRootEvent.NavigateToNotificationPreferences -> onOpenNotificationPreferences()
                SettingsRootEvent.NavigateToServiceCatalog -> onOpenServiceCatalog()
                SettingsRootEvent.LoggedOut -> onCerrarSesion()
            }
        }
    }

    SettingsRootScreen(state = state, onIntent = viewModel::onIntent)
}
