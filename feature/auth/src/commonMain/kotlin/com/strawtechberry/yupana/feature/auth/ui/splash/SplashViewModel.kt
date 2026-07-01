package com.strawtechberry.yupana.feature.auth.ui.splash

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.model.EstadoSesion
import com.strawtechberry.yupana.feature.auth.domain.usecase.ObservarEstadoSesionUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Decide el destino inicial: espera a que el SDK termine de restaurar la sesión
 * ([EstadoSesion.Inicializando]) y luego navega al Dashboard o al Login.
 */
class SplashViewModel(
    private val observarEstadoSesion: ObservarEstadoSesionUseCase,
) : MviViewModel<SplashUiState, SplashIntent, SplashEvent>(SplashUiState) {

    init {
        viewModelScope.launch {
            val estado = observarEstadoSesion().first { it != EstadoSesion.Inicializando }
            when (estado) {
                EstadoSesion.Autenticada -> sendEvent(SplashEvent.NavegarADashboard)
                else -> sendEvent(SplashEvent.NavegarALogin)
            }
        }
    }

    override fun onIntent(intent: SplashIntent) = Unit
}
