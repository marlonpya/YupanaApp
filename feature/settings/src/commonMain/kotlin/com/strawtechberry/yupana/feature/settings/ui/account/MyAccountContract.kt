package com.strawtechberry.yupana.feature.settings.ui.account

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

data class MyAccountUiState(
    val isLoading: Boolean = true,
    val email: String? = null,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isChangingPassword: Boolean = false,
    val passwordChanged: Boolean = false,
    val error: String? = null,
) : UiState {
    val canSubmit: Boolean
        get() = currentPassword.isNotBlank() &&
            newPassword.length >= 6 &&
            newPassword == confirmPassword &&
            !isChangingPassword
}

sealed interface MyAccountIntent : UiIntent {
    data object BackClicked : MyAccountIntent
    data class CurrentPasswordChanged(val value: String) : MyAccountIntent
    data class NewPasswordChanged(val value: String) : MyAccountIntent
    data class ConfirmPasswordChanged(val value: String) : MyAccountIntent
    data object ChangePasswordClicked : MyAccountIntent
}

sealed interface MyAccountEvent : UiEvent {
    data object NavigateBack : MyAccountEvent
}
