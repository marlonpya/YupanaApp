package com.strawtechberry.yupana.feature.auth.ui.register

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.RegisterUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.Validations
import com.strawtechberry.yupana.feature.auth.ui.common.errorMessage
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val register: RegisterUseCase,
) : MviViewModel<RegisterUiState, RegisterIntent, RegisterEvent>(RegisterUiState()) {

    override fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.EmailChanged ->
                setState { copy(email = intent.value, emailError = null, formError = null) }
            is RegisterIntent.PasswordChanged ->
                setState { copy(password = intent.value, passwordError = null, confirmError = null, formError = null) }
            is RegisterIntent.ConfirmPasswordChanged ->
                setState { copy(confirmPassword = intent.value, confirmError = null, formError = null) }
            RegisterIntent.Submit -> submit()
            RegisterIntent.NavigateToLogin -> sendEvent(RegisterEvent.NavigateToLogin)
        }
    }

    private fun submit() {
        val state = currentState
        val emailError = Validations.emailError(state.email)
        val passwordError = Validations.passwordError(state.password)
        val confirmError = Validations.confirmPasswordError(state.password, state.confirmPassword)
        if (emailError != null || passwordError != null || confirmError != null) {
            setState { copy(emailError = emailError, passwordError = passwordError, confirmError = confirmError) }
            return
        }

        setState { copy(isLoading = true, formError = null, confirmationNotice = null) }
        viewModelScope.launch {
            register(state.email, state.password).fold(
                onSuccess = { session ->
                    setState { copy(isLoading = false) }
                    if (session != null) {
                        // Autoconfirm active: immediate session → to Dashboard.
                        sendEvent(RegisterEvent.NavigateToDashboard)
                    } else {
                        // The project requires confirming the email: notify and wait.
                        setState {
                            copy(confirmationNotice = "Te enviamos un correo para confirmar tu cuenta. Confírmalo e inicia sesión.")
                        }
                    }
                },
                onFailure = { error ->
                    setState { copy(isLoading = false, formError = errorMessage(error)) }
                },
            )
        }
    }
}
