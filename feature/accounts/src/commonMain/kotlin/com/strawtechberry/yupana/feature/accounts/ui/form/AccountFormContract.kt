package com.strawtechberry.yupana.feature.accounts.ui.form

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState

/** Account form state. `accountId == null` means create mode. */
data class AccountFormUiState(
    val accountId: String? = null,
    val serviceId: String? = null,
    val serviceName: String? = null,
    val serviceColor: String? = null,
    val alias: String = "",
    val monthlyCost: String = "",
    val numProfiles: String = "1",
    val billingDay: String = "",
    val notes: String = "",
    val aliasError: String? = null,
    val serviceError: String? = null,
    val isLoading: Boolean = false,
    val isLoadingAccount: Boolean = false,
    val formError: String? = null,
) : UiState {
    val isEditMode: Boolean get() = accountId != null
    val isSaveEnabled: Boolean get() = alias.isNotBlank() && serviceId != null && !isLoading
}

/** User actions on the account form. */
sealed interface AccountFormIntent : UiIntent {
    data class AliasChanged(val value: String) : AccountFormIntent
    data class MonthlyCostChanged(val value: String) : AccountFormIntent
    data class NumProfilesChanged(val value: String) : AccountFormIntent
    data class BillingDayChanged(val value: String) : AccountFormIntent
    data class NotesChanged(val value: String) : AccountFormIntent
    data object PickServiceClicked : AccountFormIntent
    data class ServiceSelected(val serviceId: String) : AccountFormIntent
    data object Submit : AccountFormIntent
    data object BackClicked : AccountFormIntent
}

/** One-time effects of the account form. */
sealed interface AccountFormEvent : UiEvent {
    data object NavigateBack : AccountFormEvent
    data object NavigateToServicePicker : AccountFormEvent
    data object SavedSuccessfully : AccountFormEvent
}
