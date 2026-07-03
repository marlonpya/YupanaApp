package com.strawtechberry.yupana.feature.clients.ui.detail

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientAssignmentsUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientUseCase
import com.strawtechberry.yupana.feature.clients.ui.common.errorMessage
import kotlinx.coroutines.launch

class ClientDetailViewModel(
    private val clientId: String,
    private val getClient: GetClientUseCase,
    private val getClientAssignments: GetClientAssignmentsUseCase,
) : MviViewModel<ClientDetailUiState, ClientDetailIntent, ClientDetailEvent>(ClientDetailUiState(clientId = clientId)) {

    init {
        load()
    }

    override fun onIntent(intent: ClientDetailIntent) {
        when (intent) {
            ClientDetailIntent.Retry -> load()
            ClientDetailIntent.EditClicked -> sendEvent(ClientDetailEvent.NavigateToEdit(clientId))
            ClientDetailIntent.BackClicked -> sendEvent(ClientDetailEvent.NavigateBack)
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getClient(clientId).fold(
                onSuccess = { client ->
                    getClientAssignments(clientId).fold(
                        onSuccess = { assignments ->
                            setState {
                                copy(
                                    isLoading = false,
                                    client = client,
                                    activeAssignments = assignments.filter { it.status == "active" },
                                    cancelledAssignments = assignments.filter { it.status == "cancelled" },
                                )
                            }
                        },
                        onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
                    )
                },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
