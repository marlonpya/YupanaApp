package com.strawtechberry.yupana.feature.auth.ui.register

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.RegistrarUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.Validaciones
import com.strawtechberry.yupana.feature.auth.ui.common.mensajeDeError
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registrar: RegistrarUseCase,
) : MviViewModel<RegisterUiState, RegisterIntent, RegisterEvent>(RegisterUiState()) {

    override fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.EmailCambiado ->
                setState { copy(email = intent.valor, emailError = null, formError = null) }
            is RegisterIntent.PasswordCambiado ->
                setState { copy(password = intent.valor, passwordError = null, confirmError = null, formError = null) }
            is RegisterIntent.ConfirmCambiado ->
                setState { copy(confirmPassword = intent.valor, confirmError = null, formError = null) }
            RegisterIntent.Enviar -> enviar()
            RegisterIntent.IrALogin -> sendEvent(RegisterEvent.NavegarALogin)
        }
    }

    private fun enviar() {
        val estado = currentState
        val emailError = Validaciones.emailError(estado.email)
        val passwordError = Validaciones.passwordError(estado.password)
        val confirmError = Validaciones.confirmarPasswordError(estado.password, estado.confirmPassword)
        if (emailError != null || passwordError != null || confirmError != null) {
            setState { copy(emailError = emailError, passwordError = passwordError, confirmError = confirmError) }
            return
        }

        setState { copy(cargando = true, formError = null, avisoConfirmacion = null) }
        viewModelScope.launch {
            registrar(estado.email, estado.password).fold(
                onSuccess = { sesion ->
                    setState { copy(cargando = false) }
                    if (sesion != null) {
                        // Autoconfirm activo: sesión inmediata → al Dashboard.
                        sendEvent(RegisterEvent.NavegarADashboard)
                    } else {
                        // El proyecto exige confirmar el correo: avisamos y esperamos.
                        setState {
                            copy(avisoConfirmacion = "Te enviamos un correo para confirmar tu cuenta. Confírmalo e inicia sesión.")
                        }
                    }
                },
                onFailure = { error ->
                    setState { copy(cargando = false, formError = mensajeDeError(error)) }
                },
            )
        }
    }
}
