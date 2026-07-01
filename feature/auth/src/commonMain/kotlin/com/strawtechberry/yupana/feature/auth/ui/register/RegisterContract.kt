package com.strawtechberry.yupana.feature.auth.ui.register

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Estado de la pantalla de Registro. */
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val formError: String? = null,
    /** Aviso cuando el registro requiere confirmar el correo (sin sesión inmediata). */
    val avisoConfirmacion: String? = null,
    val cargando: Boolean = false,
) : UiState

/** Acciones del usuario en Registro. */
sealed interface RegisterIntent : UiIntent {
    data class EmailCambiado(val valor: String) : RegisterIntent
    data class PasswordCambiado(val valor: String) : RegisterIntent
    data class ConfirmCambiado(val valor: String) : RegisterIntent
    data object Enviar : RegisterIntent
    data object IrALogin : RegisterIntent
}

/** Efectos de una sola vez de Registro. */
sealed interface RegisterEvent : UiEvent {
    data object NavegarADashboard : RegisterEvent
    data object NavegarALogin : RegisterEvent
}
