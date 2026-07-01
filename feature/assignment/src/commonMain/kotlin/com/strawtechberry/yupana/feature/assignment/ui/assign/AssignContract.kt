package com.strawtechberry.yupana.feature.assignment.ui.assign

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import com.strawtechberry.yupana.feature.clients.domain.model.Client

/**
 * Assign screen state. `accountId`/`profileId` come pre-filled (and locked) when
 * entering from the account detail screen's free profile; both null means the "nueva
 * asignación desde cero" entry point.
 */
data class AssignUiState(
    val accountId: String? = null,
    val accountLocked: Boolean = false,
    val accountLabel: String? = null,
    val profileId: String? = null,
    val profileLocked: Boolean = false,
    val profileLabel: String? = null,

    val accountGroups: List<AccountGroup> = emptyList(),
    val availableProfiles: List<Profile> = emptyList(),

    val clients: List<Client> = emptyList(),
    val clientQuery: String = "",
    val selectedClientId: String? = null,
    val selectedClientName: String? = null,

    val priceCharged: String = "",
    val dueDate: String = "",

    val showCreateClientDialog: Boolean = false,
    val newClientName: String = "",
    val newClientContact: String = "",
    val isCreatingClient: Boolean = false,
    val createClientError: String? = null,

    val isLoadingOptions: Boolean = false,
    val isSaving: Boolean = false,
    val formError: String? = null,
) : UiState {
    val filteredClients: List<Client>
        get() = if (clientQuery.isBlank()) clients else clients.filter { it.name.contains(clientQuery, ignoreCase = true) }

    val isSaveEnabled: Boolean
        get() = profileId != null && selectedClientId != null && priceCharged.isNotBlank() && dueDate.isNotBlank() && !isSaving
}

/** User actions on the assign screen. */
sealed interface AssignIntent : UiIntent {
    data class AccountSelected(val accountId: String, val label: String) : AssignIntent
    data class ProfileSelected(val profileId: String, val label: String) : AssignIntent
    data class ClientQueryChanged(val value: String) : AssignIntent
    data class ClientSelected(val clientId: String) : AssignIntent
    data object CreateClientClicked : AssignIntent
    data object DismissCreateClientDialog : AssignIntent
    data class NewClientNameChanged(val value: String) : AssignIntent
    data class NewClientContactChanged(val value: String) : AssignIntent
    data object ConfirmCreateClient : AssignIntent
    data class PriceChanged(val value: String) : AssignIntent
    data class DueDateChanged(val value: String) : AssignIntent
    data object Submit : AssignIntent
    data object BackClicked : AssignIntent
}

/** One-time effects of the assign screen. */
sealed interface AssignEvent : UiEvent {
    data object NavigateBack : AssignEvent
    data object AssignedSuccessfully : AssignEvent
}
