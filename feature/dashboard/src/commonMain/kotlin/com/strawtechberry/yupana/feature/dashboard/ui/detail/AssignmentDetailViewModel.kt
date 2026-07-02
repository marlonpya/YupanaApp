package com.strawtechberry.yupana.feature.dashboard.ui.detail

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.assignment.domain.usecase.EditAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.LiberateAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.RenewAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.ui.common.errorMessage
import kotlinx.coroutines.launch

class AssignmentDetailViewModel(
    private val assignmentId: String,
    private val getUpcomingExpiration: GetUpcomingExpirationUseCase,
    private val renewAssignment: RenewAssignmentUseCase,
    private val editAssignment: EditAssignmentUseCase,
    private val liberateAssignment: LiberateAssignmentUseCase,
) : MviViewModel<AssignmentDetailUiState, AssignmentDetailIntent, AssignmentDetailEvent>(
    AssignmentDetailUiState(assignmentId = assignmentId),
) {

    init {
        load()
    }

    override fun onIntent(intent: AssignmentDetailIntent) {
        when (intent) {
            AssignmentDetailIntent.Retry -> load()
            AssignmentDetailIntent.BackClicked -> sendEvent(AssignmentDetailEvent.NavigateBack)
            AssignmentDetailIntent.RenewClicked -> renew()
            AssignmentDetailIntent.EditClicked -> openEditDialog()
            AssignmentDetailIntent.DismissEditDialog -> setState { copy(showEditDialog = false, editError = null) }
            is AssignmentDetailIntent.EditPriceChanged -> setState { copy(editPrice = intent.value, editError = null) }
            is AssignmentDetailIntent.EditDueDateChanged -> setState { copy(editDueDate = intent.value, editError = null) }
            AssignmentDetailIntent.ConfirmEdit -> confirmEdit()
            AssignmentDetailIntent.LiberateClicked -> setState { copy(showLiberateConfirm = true) }
            AssignmentDetailIntent.DismissLiberateConfirm -> setState { copy(showLiberateConfirm = false) }
            AssignmentDetailIntent.ConfirmLiberate -> confirmLiberate()
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getUpcomingExpiration(assignmentId).fold(
                onSuccess = { expiration -> setState { copy(isLoading = false, expiration = expiration) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }

    private fun openEditDialog() {
        val expiration = currentState.expiration ?: return
        setState {
            copy(
                showEditDialog = true,
                editPrice = "${expiration.priceCharged ?: ""}",
                editDueDate = expiration.dueDate.orEmpty(),
                editError = null,
            )
        }
    }

    private fun renew() {
        val dueDate = currentState.expiration?.dueDate ?: return
        setState { copy(isRenewing = true, error = null) }
        viewModelScope.launch {
            renewAssignment(assignmentId, dueDate).fold(
                onSuccess = {
                    setState { copy(isRenewing = false) }
                    sendEvent(AssignmentDetailEvent.ActionCompleted)
                },
                onFailure = { error -> setState { copy(isRenewing = false, error = errorMessage(error)) } },
            )
        }
    }

    private fun confirmEdit() {
        val state = currentState
        val price = state.editPrice.trim().toDoubleOrNull()
        val dueDate = state.editDueDate.trim()
        if (price == null || dueDate.isBlank()) {
            setState { copy(editError = "Completa precio y fecha") }
            return
        }
        setState { copy(isSavingEdit = true, editError = null) }
        viewModelScope.launch {
            editAssignment(assignmentId, price, dueDate).fold(
                onSuccess = {
                    setState { copy(isSavingEdit = false, showEditDialog = false) }
                    sendEvent(AssignmentDetailEvent.ActionCompleted)
                },
                onFailure = { error -> setState { copy(isSavingEdit = false, editError = errorMessage(error)) } },
            )
        }
    }

    private fun confirmLiberate() {
        setState { copy(isLiberating = true) }
        viewModelScope.launch {
            liberateAssignment(assignmentId).fold(
                onSuccess = {
                    setState { copy(isLiberating = false, showLiberateConfirm = false) }
                    sendEvent(AssignmentDetailEvent.ActionCompleted)
                },
                onFailure = { error ->
                    setState { copy(isLiberating = false, showLiberateConfirm = false, error = errorMessage(error)) }
                },
            )
        }
    }
}
