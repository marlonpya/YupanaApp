package com.strawtechberry.yupana.feature.clients.ui.detail

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.clients.domain.model.Client
import com.strawtechberry.yupana.feature.clients.domain.model.ClientAssignment

/** Client detail screen state. */
data class ClientDetailUiState(
    val clientId: String,
    val client: Client? = null,
    val activeAssignments: List<ClientAssignment> = emptyList(),
    val cancelledAssignments: List<ClientAssignment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User actions on the client detail screen. */
sealed interface ClientDetailIntent : UiIntent {
    data object Retry : ClientDetailIntent
    data object EditClicked : ClientDetailIntent
    data object BackClicked : ClientDetailIntent
}

/** One-time effects of the client detail screen. */
sealed interface ClientDetailEvent : UiEvent {
    data object NavigateBack : ClientDetailEvent
    data class NavigateToEdit(val clientId: String) : ClientDetailEvent
}
