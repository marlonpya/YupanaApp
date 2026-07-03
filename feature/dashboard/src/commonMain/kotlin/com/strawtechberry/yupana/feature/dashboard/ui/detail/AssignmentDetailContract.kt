package com.strawtechberry.yupana.feature.dashboard.ui.detail

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

data class AssignmentDetailUiState(
    val assignmentId: String,
    val expiration: UpcomingExpiration? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRenewing: Boolean = false,
    val showEditDialog: Boolean = false,
    val editPrice: String = "",
    val editDueDate: String = "",
    val isSavingEdit: Boolean = false,
    val editError: String? = null,
    val showLiberateConfirm: Boolean = false,
    val isLiberating: Boolean = false,
) : UiState

/** User actions on the assignment detail screen. */
sealed interface AssignmentDetailIntent : UiIntent {
    data object Retry : AssignmentDetailIntent
    data object BackClicked : AssignmentDetailIntent
    data object RenewClicked : AssignmentDetailIntent
    data object EditClicked : AssignmentDetailIntent
    data object DismissEditDialog : AssignmentDetailIntent
    data class EditPriceChanged(val value: String) : AssignmentDetailIntent
    data class EditDueDateChanged(val value: String) : AssignmentDetailIntent
    data object ConfirmEdit : AssignmentDetailIntent
    data object LiberateClicked : AssignmentDetailIntent
    data object DismissLiberateConfirm : AssignmentDetailIntent
    data object ConfirmLiberate : AssignmentDetailIntent
    data object MoveClicked : AssignmentDetailIntent
}

/** One-time effects of the assignment detail screen. */
sealed interface AssignmentDetailEvent : UiEvent {
    data object NavigateBack : AssignmentDetailEvent

    /** Renew/edit/liberate succeeded — pop back to the Dashboard, which reloads on re-entry. */
    data object ActionCompleted : AssignmentDetailEvent

    data class NavigateToMove(val assignmentId: String) : AssignmentDetailEvent
}
