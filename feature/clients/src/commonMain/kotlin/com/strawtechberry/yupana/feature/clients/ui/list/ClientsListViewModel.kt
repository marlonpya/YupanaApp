package com.strawtechberry.yupana.feature.clients.ui.list

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientsUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.SearchClientsUseCase
import com.strawtechberry.yupana.feature.clients.ui.common.errorMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ClientsListViewModel(
    private val getClients: GetClientsUseCase,
    private val searchClients: SearchClientsUseCase,
) : MviViewModel<ClientsListUiState, ClientsListIntent, ClientsListEvent>(ClientsListUiState()) {

    private var queryJob: Job? = null

    override fun onIntent(intent: ClientsListIntent) {
        when (intent) {
            is ClientsListIntent.QueryChanged -> {
                setState { copy(query = intent.value) }
                runQuery(intent.value)
            }
            ClientsListIntent.Retry -> runQuery(currentState.query)
            ClientsListIntent.CreateClicked -> sendEvent(ClientsListEvent.NavigateToCreate)
            is ClientsListIntent.ClientClicked -> sendEvent(ClientsListEvent.NavigateToEdit(intent.id))
        }
    }

    /** Loads the list; called from the Route each time it re-enters composition. */
    fun load() {
        runQuery(currentState.query)
    }

    private fun runQuery(query: String) {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            val result = if (query.isBlank()) getClients() else searchClients(query)
            result.fold(
                onSuccess = { clients -> setState { copy(isLoading = false, clients = clients) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
