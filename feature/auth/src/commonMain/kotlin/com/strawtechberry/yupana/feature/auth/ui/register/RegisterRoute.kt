package com.strawtechberry.yupana.feature.auth.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/** Register route: connects the [RegisterViewModel] with the UI and maps events to navigation. */
@Composable
fun RegisterRoute(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.NavigateToDashboard -> onRegisterSuccess()
                RegisterEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    RegisterScreen(state = state, onIntent = viewModel::onIntent)
}
