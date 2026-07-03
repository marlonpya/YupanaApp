package com.strawtechberry.yupana.feature.auth.ui.resetpassword

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Reset password screen state. */
data class ResetPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val formError: String? = null,
    val isLoading: Boolean = false,
    val isSent: Boolean = false,
) : UiState

/** User actions on Reset password. */
sealed interface ResetPasswordIntent : UiIntent {
    data class EmailChanged(val value: String) : ResetPasswordIntent
    data object Submit : ResetPasswordIntent
    data object BackClicked : ResetPasswordIntent
}

/** One-time effects of Reset password. */
sealed interface ResetPasswordEvent : UiEvent {
    data object NavigateBack : ResetPasswordEvent
}
