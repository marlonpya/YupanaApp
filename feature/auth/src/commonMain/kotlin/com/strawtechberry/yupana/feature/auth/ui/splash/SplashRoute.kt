package com.strawtechberry.yupana.feature.auth.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel

/** Splash route: observes the session (via the ViewModel) and navigates to the resolved destination. */
@Composable
fun SplashRoute(
    onSessionActive: () -> Unit,
    onNoSession: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SplashEvent.NavigateToDashboard -> onSessionActive()
                SplashEvent.NavigateToLogin -> onNoSession()
            }
        }
    }

    SplashScreen()
}
