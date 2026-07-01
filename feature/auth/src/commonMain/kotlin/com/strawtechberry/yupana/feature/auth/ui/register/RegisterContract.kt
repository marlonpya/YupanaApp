package com.strawtechberry.yupana.feature.auth.ui.register

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Register screen state. */
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val formError: String? = null,
    /** Notice shown when registration requires confirming the email (no immediate session). */
    val confirmationNotice: String? = null,
    val isLoading: Boolean = false,
) : UiState

/** User actions on Register. */
sealed interface RegisterIntent : UiIntent {
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data class ConfirmPasswordChanged(val value: String) : RegisterIntent
    data object Submit : RegisterIntent
    data object NavigateToLogin : RegisterIntent
}

/** One-time effects of Register. */
sealed interface RegisterEvent : UiEvent {
    data object NavigateToDashboard : RegisterEvent
    data object NavigateToLogin : RegisterEvent
}
