package com.strawtechberry.yupana.feature.clients.ui.form

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.clients.domain.usecase.CreateClientUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.UpdateClientUseCase
import com.strawtechberry.yupana.feature.clients.ui.common.errorMessage
import kotlinx.coroutines.launch

class ClientFormViewModel(
    clientId: String?,
    private val getClient: GetClientUseCase,
    private val createClient: CreateClientUseCase,
    private val updateClient: UpdateClientUseCase,
) : MviViewModel<ClientFormUiState, ClientFormIntent, ClientFormEvent>(ClientFormUiState(clientId = clientId)) {

    init {
        if (clientId != null) loadClient(clientId)
    }

    override fun onIntent(intent: ClientFormIntent) {
        when (intent) {
            is ClientFormIntent.NameChanged -> setState { copy(name = intent.value, nameError = null, formError = null) }
            is ClientFormIntent.ContactChanged -> setState { copy(contact = intent.value, formError = null) }
            is ClientFormIntent.NotesChanged -> setState { copy(notes = intent.value, formError = null) }
            ClientFormIntent.Submit -> submit()
            ClientFormIntent.BackClicked -> sendEvent(ClientFormEvent.NavigateBack)
        }
    }

    private fun loadClient(id: String) {
        setState { copy(isLoadingClient = true) }
        viewModelScope.launch {
            getClient(id).fold(
                onSuccess = { client ->
                    setState {
                        copy(
                            isLoadingClient = false,
                            name = client.name,
                            contact = client.contact.orEmpty(),
                            notes = client.notes.orEmpty(),
                        )
                    }
                },
                onFailure = { error ->
                    setState { copy(isLoadingClient = false, formError = errorMessage(error)) }
                },
            )
        }
    }

    private fun submit() {
        val state = currentState
        if (state.name.isBlank()) {
            setState { copy(nameError = "Ingresa el nombre del cliente") }
            return
        }

        setState { copy(isLoading = true, formError = null) }
        viewModelScope.launch {
            val result = if (state.clientId != null) {
                updateClient(state.clientId, state.name, state.contact, state.notes)
            } else {
                createClient(state.name, state.contact, state.notes)
            }
            result.fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    sendEvent(ClientFormEvent.SavedSuccessfully)
                },
                onFailure = { error ->
                    setState { copy(isLoading = false, formError = errorMessage(error)) }
                },
            )
        }
    }
}
