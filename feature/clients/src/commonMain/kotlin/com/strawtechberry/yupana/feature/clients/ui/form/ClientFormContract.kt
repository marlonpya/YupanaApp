package com.strawtechberry.yupana.feature.clients.ui.form

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Client form screen state. `clientId == null` means create mode. */
data class ClientFormUiState(
    val clientId: String? = null,
    val name: String = "",
    val contact: String = "",
    val notes: String = "",
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val isLoadingClient: Boolean = false,
    val formError: String? = null,
) : UiState {
    val isEditMode: Boolean get() = clientId != null
    val isSaveEnabled: Boolean get() = name.isNotBlank() && !isLoading
}

/** User actions on the client form. */
sealed interface ClientFormIntent : UiIntent {
    data class NameChanged(val value: String) : ClientFormIntent
    data class ContactChanged(val value: String) : ClientFormIntent
    data class NotesChanged(val value: String) : ClientFormIntent
    data object Submit : ClientFormIntent
    data object BackClicked : ClientFormIntent
}

/** One-time effects of the client form. */
sealed interface ClientFormEvent : UiEvent {
    data object NavigateBack : ClientFormEvent
    data object SavedSuccessfully : ClientFormEvent
}
