package com.strawtechberry.yupana.feature.auth.ui.splash

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Splash only waits for the session to resolve; its state is constant. */
data object SplashUiState : UiState

/** Splash doesn't receive user actions. */
sealed interface SplashIntent : UiIntent

/** Destination decided based on whether there's an active session or not. */
sealed interface SplashEvent : UiEvent {
    data object NavigateToDashboard : SplashEvent
    data object NavigateToLogin : SplashEvent
}
