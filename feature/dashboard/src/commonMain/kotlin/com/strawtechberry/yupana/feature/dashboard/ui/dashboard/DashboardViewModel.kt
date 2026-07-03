package com.strawtechberry.yupana.feature.dashboard.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationsUseCase
import com.strawtechberry.yupana.feature.assignment.ui.common.errorMessage
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getUpcomingExpirations: GetUpcomingExpirationsUseCase,
) : MviViewModel<DashboardUiState, DashboardIntent, DashboardEvent>(DashboardUiState()) {

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Retry -> load()
            is DashboardIntent.ExpirationClicked -> sendEvent(DashboardEvent.NavigateToDetail(intent.assignmentId))
            DashboardIntent.NewAssignmentClicked -> sendEvent(DashboardEvent.NavigateToNewAssignment)
            DashboardIntent.SeeAllClicked -> sendEvent(DashboardEvent.NavigateToAllExpirations)
        }
    }

    /** Loads the expirations; called from the Route each time it re-enters composition. */
    fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getUpcomingExpirations().fold(
                onSuccess = { expirations -> setState { copy(isLoading = false, expirations = expirations) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
