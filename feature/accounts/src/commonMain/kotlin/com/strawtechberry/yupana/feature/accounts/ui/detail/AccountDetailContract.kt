package com.strawtechberry.yupana.feature.accounts.ui.detail

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile

/** Account detail screen state. */
data class AccountDetailUiState(
    val accountId: String,
    val account: Account? = null,
    val profiles: List<Profile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User actions on the account detail screen. */
sealed interface AccountDetailIntent : UiIntent {
    data object Retry : AccountDetailIntent
    data object EditClicked : AccountDetailIntent
    data object BackClicked : AccountDetailIntent

    /** Tapped "Asignar cliente" on a free profile. */
    data class AssignClicked(val profileId: String) : AccountDetailIntent
}

/** One-time effects of the account detail screen. */
sealed interface AccountDetailEvent : UiEvent {
    data object NavigateBack : AccountDetailEvent
    data class NavigateToEdit(val accountId: String) : AccountDetailEvent
    data class NavigateToAssign(val accountId: String, val profileId: String) : AccountDetailEvent
}
