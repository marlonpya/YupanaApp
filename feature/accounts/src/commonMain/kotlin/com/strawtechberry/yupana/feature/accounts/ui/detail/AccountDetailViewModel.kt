package com.strawtechberry.yupana.feature.accounts.ui.detail

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountProfilesUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountUseCase
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage
import kotlinx.coroutines.launch

class AccountDetailViewModel(
    private val accountId: String,
    private val getAccount: GetAccountUseCase,
    private val getAccountProfiles: GetAccountProfilesUseCase,
) : MviViewModel<AccountDetailUiState, AccountDetailIntent, AccountDetailEvent>(AccountDetailUiState(accountId = accountId)) {

    init {
        load()
    }

    override fun onIntent(intent: AccountDetailIntent) {
        when (intent) {
            AccountDetailIntent.Retry -> load()
            AccountDetailIntent.EditClicked -> sendEvent(AccountDetailEvent.NavigateToEdit(accountId))
            AccountDetailIntent.BackClicked -> sendEvent(AccountDetailEvent.NavigateBack)
            is AccountDetailIntent.AssignClicked -> Unit // Grupo 4 aún no existe; no-op deliberado.
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getAccount(accountId).fold(
                onSuccess = { account ->
                    getAccountProfiles(accountId).fold(
                        onSuccess = { profiles -> setState { copy(isLoading = false, account = account, profiles = profiles) } },
                        onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
                    )
                },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
