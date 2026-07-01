package com.strawtechberry.yupana.feature.accounts.ui.list

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup

/** "Mis cuentas" screen state: accounts grouped by service. */
data class AccountsListUiState(
    val groups: List<AccountGroup> = emptyList(),
    val expandedServiceIds: Set<String> = emptySet(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User actions on the accounts list. */
sealed interface AccountsListIntent : UiIntent {
    data class QueryChanged(val value: String) : AccountsListIntent
    data object Retry : AccountsListIntent
    data object CreateClicked : AccountsListIntent
    data object CatalogClicked : AccountsListIntent
    data object NewAssignmentClicked : AccountsListIntent
    data class GroupClicked(val serviceId: String) : AccountsListIntent
    data class AccountClicked(val accountId: String) : AccountsListIntent
}

/** One-time effects of the accounts list. */
sealed interface AccountsListEvent : UiEvent {
    data object NavigateToCreate : AccountsListEvent
    data object NavigateToCatalog : AccountsListEvent
    data object NavigateToNewAssignment : AccountsListEvent
    data class NavigateToDetail(val accountId: String) : AccountsListEvent
}
