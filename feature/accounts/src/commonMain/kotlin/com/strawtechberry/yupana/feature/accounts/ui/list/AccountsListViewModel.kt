package com.strawtechberry.yupana.feature.accounts.ui.list

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountGroupsUseCase
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AccountsListViewModel(
    private val getAccountGroups: GetAccountGroupsUseCase,
) : MviViewModel<AccountsListUiState, AccountsListIntent, AccountsListEvent>(AccountsListUiState()) {

    private var queryJob: Job? = null

    override fun onIntent(intent: AccountsListIntent) {
        when (intent) {
            is AccountsListIntent.QueryChanged -> {
                setState { copy(query = intent.value) }
                runQuery(intent.value)
            }
            AccountsListIntent.Retry -> runQuery(currentState.query)
            AccountsListIntent.CreateClicked -> sendEvent(AccountsListEvent.NavigateToCreate)
            AccountsListIntent.CatalogClicked -> sendEvent(AccountsListEvent.NavigateToCatalog)
            AccountsListIntent.NewAssignmentClicked -> sendEvent(AccountsListEvent.NavigateToNewAssignment)
            is AccountsListIntent.GroupClicked -> setState {
                val expanded = expandedServiceIds
                copy(
                    expandedServiceIds = if (intent.serviceId in expanded) expanded - intent.serviceId else expanded + intent.serviceId,
                )
            }
            is AccountsListIntent.AccountClicked -> sendEvent(AccountsListEvent.NavigateToDetail(intent.accountId))
        }
    }

    /** Loads the groups; called from the Route each time it re-enters composition. */
    fun load() {
        runQuery(currentState.query)
    }

    private fun runQuery(query: String) {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getAccountGroups(query).fold(
                onSuccess = { groups -> setState { copy(isLoading = false, groups = groups) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
