package com.strawtechberry.yupana.feature.auth.ui.splash

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.model.SessionState
import com.strawtechberry.yupana.feature.auth.domain.usecase.ObserveSessionStateUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Decides the initial destination: waits for the SDK to finish restoring the session
 * ([SessionState.Initializing]) and then navigates to Dashboard or Login.
 */
class SplashViewModel(
    private val observeSessionState: ObserveSessionStateUseCase,
) : MviViewModel<SplashUiState, SplashIntent, SplashEvent>(SplashUiState) {

    init {
        viewModelScope.launch {
            val state = observeSessionState().first { it != SessionState.Initializing }
            when (state) {
                SessionState.Authenticated -> sendEvent(SplashEvent.NavigateToDashboard)
                else -> sendEvent(SplashEvent.NavigateToLogin)
            }
        }
    }

    override fun onIntent(intent: SplashIntent) = Unit
}
