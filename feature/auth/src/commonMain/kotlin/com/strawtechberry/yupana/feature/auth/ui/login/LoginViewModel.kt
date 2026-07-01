package com.strawtechberry.yupana.feature.auth.ui.login

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.IniciarSesionUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.Validaciones
import com.strawtechberry.yupana.feature.auth.ui.common.mensajeDeError
import kotlinx.coroutines.launch

class LoginViewModel(
    private val iniciarSesion: IniciarSesionUseCase,
) : MviViewModel<LoginUiState, LoginIntent, LoginEvent>(LoginUiState()) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailCambiado ->
                setState { copy(email = intent.valor, emailError = null, formError = null) }
            is LoginIntent.PasswordCambiado ->
                setState { copy(password = intent.valor, passwordError = null, formError = null) }
            LoginIntent.Enviar -> enviar()
            LoginIntent.IrARegistro -> sendEvent(LoginEvent.NavegarARegistro)
            LoginIntent.OlvidePassword -> sendEvent(LoginEvent.NavegarAOlvidePassword)
        }
    }

    private fun enviar() {
        val estado = currentState
        val emailError = Validaciones.emailError(estado.email)
        val passwordError = Validaciones.passwordError(estado.password)
        if (emailError != null || passwordError != null) {
            setState { copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        setState { copy(cargando = true, formError = null) }
        viewModelScope.launch {
            iniciarSesion(estado.email, estado.password).fold(
                onSuccess = {
                    setState { copy(cargando = false) }
                    sendEvent(LoginEvent.NavegarADashboard)
                },
                onFailure = { error ->
                    setState { copy(cargando = false, formError = mensajeDeError(error)) }
                },
            )
        }
    }
}
