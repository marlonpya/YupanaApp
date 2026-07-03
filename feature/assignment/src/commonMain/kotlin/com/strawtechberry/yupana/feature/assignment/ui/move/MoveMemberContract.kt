package com.strawtechberry.yupana.feature.assignment.ui.move

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountWithOccupancy
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile

/** "Mover integrante" screen state. */
data class MoveMemberUiState(
    val assignmentId: String,
    val clientName: String? = null,
    val sourceServiceName: String? = null,
    val sourceAccountAlias: String? = null,
    val sourceProfileLabel: String? = null,
    val priceCharged: Double? = null,
    val dueDate: String? = null,
    val serviceId: String? = null,
    val currentAccountId: String? = null,

    val candidateAccounts: List<AccountWithOccupancy> = emptyList(),
    val selectedAccountId: String? = null,
    val selectedAccountAlias: String? = null,

    val availableProfiles: List<Profile> = emptyList(),
    val selectedProfileId: String? = null,
    val selectedProfileLabel: String? = null,

    val isLoading: Boolean = false,
    val isLoadingProfiles: Boolean = false,
    val isMoving: Boolean = false,
    val error: String? = null,
    val showConfirm: Boolean = false,
) : UiState {
    val canMove: Boolean get() = selectedProfileId != null && !isMoving
}

/** User actions on "Mover integrante". */
sealed interface MoveMemberIntent : UiIntent {
    data object Retry : MoveMemberIntent
    data object BackClicked : MoveMemberIntent
    data class AccountSelected(val accountId: String, val alias: String) : MoveMemberIntent
    data object ChangeAccountClicked : MoveMemberIntent
    data class ProfileSelected(val profileId: String, val label: String) : MoveMemberIntent
    data object MoveClicked : MoveMemberIntent
    data object DismissConfirm : MoveMemberIntent
    data object ConfirmMove : MoveMemberIntent
    data object CreateAccountClicked : MoveMemberIntent
}

/** One-time effects of "Mover integrante". */
sealed interface MoveMemberEvent : UiEvent {
    data object NavigateBack : MoveMemberEvent
    data object MoveCompleted : MoveMemberEvent
    data object NavigateToCreateAccount : MoveMemberEvent
}
