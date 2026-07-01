package com.strawtechberry.yupana.feature.assignment.ui.assign

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountGroupsUseCase
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentError
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentException
import com.strawtechberry.yupana.feature.assignment.domain.usecase.AssignProfileToClientUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetAvailableProfilesUseCase
import com.strawtechberry.yupana.feature.assignment.ui.common.errorMessage
import com.strawtechberry.yupana.feature.clients.domain.usecase.CreateClientUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientsUseCase
import kotlinx.coroutines.launch

class AssignViewModel(
    initialAccountId: String?,
    initialProfileId: String?,
    private val getAccountGroups: GetAccountGroupsUseCase,
    private val getAvailableProfiles: GetAvailableProfilesUseCase,
    private val getClients: GetClientsUseCase,
    private val createClient: CreateClientUseCase,
    private val assignProfileToClient: AssignProfileToClientUseCase,
) : MviViewModel<AssignUiState, AssignIntent, AssignEvent>(
    AssignUiState(
        accountId = initialAccountId,
        accountLocked = initialAccountId != null,
        profileId = initialProfileId,
        profileLocked = initialProfileId != null,
    ),
) {

    init {
        viewModelScope.launch {
            setState { copy(isLoadingOptions = true, formError = null) }

            getClients().fold(
                onSuccess = { clients -> setState { copy(clients = clients) } },
                onFailure = { error -> setState { copy(formError = errorMessage(error)) } },
            )

            getAccountGroups("").fold(
                onSuccess = { groups ->
                    val label = initialAccountId?.let { resolveAccountLabel(groups, it) }
                    setState { copy(accountGroups = groups, accountLabel = label ?: accountLabel) }
                },
                onFailure = { error -> setState { copy(formError = errorMessage(error)) } },
            )

            if (initialAccountId != null) {
                getAvailableProfiles(initialAccountId).fold(
                    onSuccess = { profiles ->
                        val label = profiles.find { it.id == initialProfileId }?.label
                        setState { copy(availableProfiles = profiles, profileLabel = label ?: profileLabel) }
                    },
                    onFailure = { error -> setState { copy(formError = errorMessage(error)) } },
                )
            }

            setState { copy(isLoadingOptions = false) }
        }
    }

    override fun onIntent(intent: AssignIntent) {
        when (intent) {
            is AssignIntent.AccountSelected -> {
                setState {
                    copy(
                        accountId = intent.accountId,
                        accountLabel = intent.label,
                        profileId = null,
                        profileLabel = null,
                        availableProfiles = emptyList(),
                    )
                }
                loadAvailableProfiles(intent.accountId)
            }
            is AssignIntent.ProfileSelected -> setState { copy(profileId = intent.profileId, profileLabel = intent.label) }
            is AssignIntent.ClientQueryChanged -> setState { copy(clientQuery = intent.value) }
            is AssignIntent.ClientSelected -> {
                val client = currentState.clients.find { it.id == intent.clientId }
                setState { copy(selectedClientId = intent.clientId, selectedClientName = client?.name) }
            }
            AssignIntent.CreateClientClicked -> setState { copy(showCreateClientDialog = true, createClientError = null) }
            AssignIntent.DismissCreateClientDialog -> setState {
                copy(showCreateClientDialog = false, newClientName = "", newClientContact = "", createClientError = null)
            }
            is AssignIntent.NewClientNameChanged -> setState { copy(newClientName = intent.value, createClientError = null) }
            is AssignIntent.NewClientContactChanged -> setState { copy(newClientContact = intent.value) }
            AssignIntent.ConfirmCreateClient -> confirmCreateClient()
            is AssignIntent.PriceChanged -> setState { copy(priceCharged = intent.value, formError = null) }
            is AssignIntent.DueDateChanged -> setState { copy(dueDate = intent.value, formError = null) }
            AssignIntent.Submit -> submit()
            AssignIntent.BackClicked -> sendEvent(AssignEvent.NavigateBack)
        }
    }

    private fun loadAvailableProfiles(accountId: String) {
        viewModelScope.launch {
            setState { copy(isLoadingOptions = true) }
            getAvailableProfiles(accountId).fold(
                onSuccess = { profiles -> setState { copy(isLoadingOptions = false, availableProfiles = profiles) } },
                onFailure = { error -> setState { copy(isLoadingOptions = false, formError = errorMessage(error)) } },
            )
        }
    }

    private fun confirmCreateClient() {
        val state = currentState
        if (state.newClientName.isBlank()) {
            setState { copy(createClientError = "Ingresa el nombre del cliente") }
            return
        }
        setState { copy(isCreatingClient = true, createClientError = null) }
        viewModelScope.launch {
            createClient(state.newClientName, state.newClientContact, null).fold(
                onSuccess = { client ->
                    setState {
                        copy(
                            isCreatingClient = false,
                            showCreateClientDialog = false,
                            newClientName = "",
                            newClientContact = "",
                            clients = clients + client,
                            selectedClientId = client.id,
                            selectedClientName = client.name,
                        )
                    }
                },
                onFailure = { error -> setState { copy(isCreatingClient = false, createClientError = errorMessage(error)) } },
            )
        }
    }

    private fun submit() {
        val state = currentState
        val profileId = state.profileId
        val clientId = state.selectedClientId
        val price = state.priceCharged.trim().toDoubleOrNull()
        val dueDate = state.dueDate.trim()

        if (profileId == null || clientId == null || price == null || dueDate.isBlank()) {
            setState { copy(formError = "Completa perfil, cliente, precio y fecha") }
            return
        }

        setState { copy(isSaving = true, formError = null) }
        viewModelScope.launch {
            assignProfileToClient(profileId, clientId, price, dueDate).fold(
                onSuccess = {
                    setState { copy(isSaving = false) }
                    sendEvent(AssignEvent.AssignedSuccessfully)
                },
                onFailure = { error ->
                    val isConflict = (error as? AssignmentException)?.error == AssignmentError.ProfileAlreadyAssigned
                    setState {
                        copy(
                            isSaving = false,
                            formError = errorMessage(error),
                            profileId = if (isConflict) null else profileId,
                            profileLabel = if (isConflict) null else profileLabel,
                        )
                    }
                    if (isConflict) currentState.accountId?.let { loadAvailableProfiles(it) }
                },
            )
        }
    }

    private fun resolveAccountLabel(groups: List<AccountGroup>, accountId: String): String? {
        for (group in groups) {
            val account = group.accounts.find { it.account.id == accountId }
            if (account != null) return "${group.service.name} · ${account.account.alias}"
        }
        return null
    }
}
