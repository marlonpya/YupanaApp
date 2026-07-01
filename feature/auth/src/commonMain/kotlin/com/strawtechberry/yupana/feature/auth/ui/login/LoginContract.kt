package com.strawtechberry.yupana.feature.auth.ui.login

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Login screen state. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val isLoading: Boolean = false,
) : UiState

/** User actions on Login. */
sealed interface LoginIntent : UiIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object Submit : LoginIntent
    data object NavigateToRegister : LoginIntent
    data object ForgotPasswordClicked : LoginIntent
}

/** One-time effects of Login. */
sealed interface LoginEvent : UiEvent {
    data object NavigateToDashboard : LoginEvent
    data object NavigateToRegister : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
}
