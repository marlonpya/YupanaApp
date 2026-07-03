package com.strawtechberry.yupana.feature.assignment.ui.move

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountGroupsUseCase
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentError
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentException
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetAssignmentMoveContextUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetAvailableProfilesUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.MoveAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.ui.common.errorMessage
import kotlinx.coroutines.launch

class MoveMemberViewModel(
    private val assignmentId: String,
    private val getUpcomingExpiration: GetUpcomingExpirationUseCase,
    private val getMoveContext: GetAssignmentMoveContextUseCase,
    private val getAccountGroups: GetAccountGroupsUseCase,
    private val getAvailableProfiles: GetAvailableProfilesUseCase,
    private val moveAssignment: MoveAssignmentUseCase,
) : MviViewModel<MoveMemberUiState, MoveMemberIntent, MoveMemberEvent>(MoveMemberUiState(assignmentId = assignmentId)) {

    init {
        load()
    }

    override fun onIntent(intent: MoveMemberIntent) {
        when (intent) {
            MoveMemberIntent.Retry -> load()
            MoveMemberIntent.BackClicked -> sendEvent(MoveMemberEvent.NavigateBack)
            is MoveMemberIntent.AccountSelected -> selectAccount(intent.accountId, intent.alias)
            MoveMemberIntent.ChangeAccountClicked -> setState {
                copy(
                    selectedAccountId = null,
                    selectedAccountAlias = null,
                    selectedProfileId = null,
                    selectedProfileLabel = null,
                    availableProfiles = emptyList(),
                )
            }
            is MoveMemberIntent.ProfileSelected ->
                setState { copy(selectedProfileId = intent.profileId, selectedProfileLabel = intent.label) }
            MoveMemberIntent.MoveClicked -> if (currentState.canMove) setState { copy(showConfirm = true) }
            MoveMemberIntent.DismissConfirm -> setState { copy(showConfirm = false) }
            MoveMemberIntent.ConfirmMove -> confirmMove()
            MoveMemberIntent.CreateAccountClicked -> sendEvent(MoveMemberEvent.NavigateToCreateAccount)
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getUpcomingExpiration(assignmentId).fold(
                onSuccess = { expiration ->
                    getMoveContext(assignmentId).fold(
                        onSuccess = { context ->
                            loadCandidates(context.serviceId, context.accountId, expiration)
                        },
                        onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
                    )
                },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }

    private suspend fun loadCandidates(serviceId: String, currentAccountId: String, expiration: UpcomingExpiration) {
        getAccountGroups("").fold(
            onSuccess = { groups: List<AccountGroup> ->
                val candidates = groups
                    .firstOrNull { it.service.id == serviceId }
                    ?.accounts
                    ?.filter { it.account.id != currentAccountId && it.occupiedProfiles < it.totalProfiles }
                    .orEmpty()
                setState {
                    copy(
                        isLoading = false,
                        clientName = expiration.clientName,
                        sourceServiceName = expiration.serviceName,
                        sourceAccountAlias = expiration.accountAlias,
                        sourceProfileLabel = expiration.profileLabel,
                        priceCharged = expiration.priceCharged,
                        dueDate = expiration.dueDate,
                        serviceId = serviceId,
                        currentAccountId = currentAccountId,
                        candidateAccounts = candidates,
                    )
                }
            },
            onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
        )
    }

    private fun selectAccount(accountId: String, alias: String) {
        setState {
            copy(
                selectedAccountId = accountId,
                selectedAccountAlias = alias,
                selectedProfileId = null,
                selectedProfileLabel = null,
                availableProfiles = emptyList(),
            )
        }
        viewModelScope.launch {
            setState { copy(isLoadingProfiles = true) }
            getAvailableProfiles(accountId).fold(
                onSuccess = { profiles -> setState { copy(isLoadingProfiles = false, availableProfiles = profiles) } },
                onFailure = { error -> setState { copy(isLoadingProfiles = false, error = errorMessage(error)) } },
            )
        }
    }

    private fun confirmMove() {
        val profileId = currentState.selectedProfileId ?: return
        setState { copy(isMoving = true, error = null) }
        viewModelScope.launch {
            moveAssignment(assignmentId, profileId).fold(
                onSuccess = {
                    setState { copy(isMoving = false, showConfirm = false) }
                    sendEvent(MoveMemberEvent.MoveCompleted)
                },
                onFailure = { error ->
                    val isConflict = (error as? AssignmentException)?.error == AssignmentError.ProfileAlreadyAssigned
                    setState {
                        copy(
                            isMoving = false,
                            showConfirm = false,
                            error = errorMessage(error),
                            selectedProfileId = if (isConflict) null else selectedProfileId,
                            selectedProfileLabel = if (isConflict) null else selectedProfileLabel,
                        )
                    }
                    if (isConflict) currentState.selectedAccountId?.let { selectAccount(it, currentState.selectedAccountAlias ?: "") }
                },
            )
        }
    }
}
