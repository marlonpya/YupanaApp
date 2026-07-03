package com.strawtechberry.yupana.feature.settings.ui.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationPreferencesRoute(
    onBack: () -> Unit,
    viewModel: NotificationPreferencesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NotificationPreferencesEvent.NavigateBack -> onBack()
            }
        }
    }

    NotificationPreferencesScreen(state = state, onIntent = viewModel::onIntent)
}
