package com.strawtechberry.yupana.feature.auth.ui.login

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.SignInUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.Validations
import com.strawtechberry.yupana.feature.auth.ui.common.errorMessage
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signIn: SignInUseCase,
) : MviViewModel<LoginUiState, LoginIntent, LoginEvent>(LoginUiState()) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged ->
                setState { copy(email = intent.value, emailError = null, formError = null) }
            is LoginIntent.PasswordChanged ->
                setState { copy(password = intent.value, passwordError = null, formError = null) }
            LoginIntent.Submit -> submit()
            LoginIntent.NavigateToRegister -> sendEvent(LoginEvent.NavigateToRegister)
            LoginIntent.ForgotPasswordClicked -> sendEvent(LoginEvent.NavigateToForgotPassword)
        }
    }

    private fun submit() {
        val state = currentState
        val emailError = Validations.emailError(state.email)
        val passwordError = Validations.passwordError(state.password)
        if (emailError != null || passwordError != null) {
            setState { copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        setState { copy(isLoading = true, formError = null) }
        viewModelScope.launch {
            signIn(state.email, state.password).fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    sendEvent(LoginEvent.NavigateToDashboard)
                },
                onFailure = { error ->
                    setState { copy(isLoading = false, formError = errorMessage(error)) }
                },
            )
        }
    }
}
