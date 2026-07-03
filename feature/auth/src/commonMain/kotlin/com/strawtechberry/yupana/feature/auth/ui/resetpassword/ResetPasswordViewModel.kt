package com.strawtechberry.yupana.feature.auth.ui.resetpassword

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.auth.domain.usecase.ResetPasswordUseCase
import com.strawtechberry.yupana.feature.auth.ui.common.Validations
import com.strawtechberry.yupana.feature.auth.ui.common.errorMessage
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPassword: ResetPasswordUseCase,
) : MviViewModel<ResetPasswordUiState, ResetPasswordIntent, ResetPasswordEvent>(ResetPasswordUiState()) {

    override fun onIntent(intent: ResetPasswordIntent) {
        when (intent) {
            is ResetPasswordIntent.EmailChanged ->
                setState { copy(email = intent.value, emailError = null, formError = null) }
            ResetPasswordIntent.Submit -> submit()
            ResetPasswordIntent.BackClicked -> sendEvent(ResetPasswordEvent.NavigateBack)
        }
    }

    private fun submit() {
        val state = currentState
        val emailError = Validations.emailError(state.email)
        if (emailError != null) {
            setState { copy(emailError = emailError) }
            return
        }

        setState { copy(isLoading = true, formError = null) }
        viewModelScope.launch {
            resetPassword(state.email).fold(
                onSuccess = {
                    setState { copy(isLoading = false, isSent = true) }
                },
                onFailure = { error ->
                    setState { copy(isLoading = false, formError = errorMessage(error)) }
                },
            )
        }
    }
}
