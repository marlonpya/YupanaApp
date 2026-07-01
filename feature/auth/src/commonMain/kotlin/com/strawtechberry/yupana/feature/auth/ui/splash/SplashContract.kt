package com.strawtechberry.yupana.feature.auth.ui.splash

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** El Splash solo espera a que se resuelva la sesión; su estado es constante. */
data object SplashUiState : UiState

/** El Splash no recibe acciones del usuario. */
sealed interface SplashIntent : UiIntent

/** Destino decidido según haya o no sesión activa. */
sealed interface SplashEvent : UiEvent {
    data object NavegarADashboard : SplashEvent
    data object NavegarALogin : SplashEvent
}
