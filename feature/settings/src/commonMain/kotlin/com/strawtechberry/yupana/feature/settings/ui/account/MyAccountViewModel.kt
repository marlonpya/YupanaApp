package com.strawtechberry.yupana.feature.settings.ui.account

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.ChangePasswordUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.errorMessage
import kotlinx.coroutines.launch

class MyAccountViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val changePassword: ChangePasswordUseCase,
) : MviViewModel<MyAccountUiState, MyAccountIntent, MyAccountEvent>(MyAccountUiState()) {

    init { load() }

    override fun onIntent(intent: MyAccountIntent) {
        when (intent) {
            MyAccountIntent.BackClicked -> sendEvent(MyAccountEvent.NavigateBack)
            is MyAccountIntent.CurrentPasswordChanged ->
                setState { copy(currentPassword = intent.value, error = null, passwordChanged = false) }
            is MyAccountIntent.NewPasswordChanged ->
                setState { copy(newPassword = intent.value, error = null, passwordChanged = false) }
            is MyAccountIntent.ConfirmPasswordChanged ->
                setState { copy(confirmPassword = intent.value, error = null, passwordChanged = false) }
            MyAccountIntent.ChangePasswordClicked -> submitChangePassword()
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val session = getCurrentUser()
            setState { copy(isLoading = false, email = session?.email) }
        }
    }

    private fun submitChangePassword() {
        val state = currentState
        val validationError = when {
            state.newPassword != state.confirmPassword -> "Las contraseñas nuevas no coinciden"
            state.newPassword.length < 6 -> "La nueva contraseña debe tener al menos 6 caracteres"
            state.currentPassword.isBlank() -> "Ingresa tu contraseña actual"
            else -> null
        }
        if (validationError != null) {
            setState { copy(error = validationError) }
            return
        }

        viewModelScope.launch {
            setState { copy(isChangingPassword = true, error = null) }
            changePassword(state.currentPassword, state.newPassword).fold(
                onSuccess = {
                    setState {
                        copy(
                            isChangingPassword = false,
                            passwordChanged = true,
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = "",
                        )
                    }
                },
                onFailure = { error -> setState { copy(isChangingPassword = false, error = errorMessage(error)) } },
            )
        }
    }
}
