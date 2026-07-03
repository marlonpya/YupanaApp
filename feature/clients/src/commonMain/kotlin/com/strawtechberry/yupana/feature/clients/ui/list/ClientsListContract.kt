package com.strawtechberry.yupana.feature.clients.ui.list

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/** Clients list screen state. */
data class ClientsListUiState(
    val clients: List<Client> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User actions on the clients list. */
sealed interface ClientsListIntent : UiIntent {
    data class QueryChanged(val value: String) : ClientsListIntent
    data object Retry : ClientsListIntent
    data object CreateClicked : ClientsListIntent
    data class ClientClicked(val id: String) : ClientsListIntent
}

/** One-time effects of the clients list. */
sealed interface ClientsListEvent : UiEvent {
    data object NavigateToCreate : ClientsListEvent
    data class NavigateToDetail(val id: String) : ClientsListEvent
}
