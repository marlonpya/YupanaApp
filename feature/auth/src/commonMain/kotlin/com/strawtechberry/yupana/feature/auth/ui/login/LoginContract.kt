package com.strawtechberry.yupana.feature.auth.ui.login

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Estado de la pantalla de Login. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val cargando: Boolean = false,
) : UiState

/** Acciones del usuario en Login. */
sealed interface LoginIntent : UiIntent {
    data class EmailCambiado(val valor: String) : LoginIntent
    data class PasswordCambiado(val valor: String) : LoginIntent
    data object Enviar : LoginIntent
    data object IrARegistro : LoginIntent
    data object OlvidePassword : LoginIntent
}

/** Efectos de una sola vez de Login. */
sealed interface LoginEvent : UiEvent {
    data object NavegarADashboard : LoginEvent
    data object NavegarARegistro : LoginEvent
    data object NavegarAOlvidePassword : LoginEvent
}
