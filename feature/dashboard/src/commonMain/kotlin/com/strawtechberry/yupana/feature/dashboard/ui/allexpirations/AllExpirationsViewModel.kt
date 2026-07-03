package com.strawtechberry.yupana.feature.dashboard.ui.allexpirations

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationsUseCase
import com.strawtechberry.yupana.feature.assignment.ui.common.errorMessage
import kotlinx.coroutines.launch

class AllExpirationsViewModel(
    private val getUpcomingExpirations: GetUpcomingExpirationsUseCase,
) : MviViewModel<AllExpirationsUiState, AllExpirationsIntent, AllExpirationsEvent>(AllExpirationsUiState()) {

    init {
        load()
    }

    override fun onIntent(intent: AllExpirationsIntent) {
        when (intent) {
            AllExpirationsIntent.Retry -> load()
            is AllExpirationsIntent.FilterChanged -> setState { copy(filter = intent.filter) }
            is AllExpirationsIntent.QueryChanged -> setState { copy(query = intent.value) }
            AllExpirationsIntent.BackClicked -> sendEvent(AllExpirationsEvent.NavigateBack)
            is AllExpirationsIntent.ExpirationClicked -> sendEvent(AllExpirationsEvent.NavigateToDetail(intent.assignmentId))
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getUpcomingExpirations().fold(
                onSuccess = { expirations -> setState { copy(isLoading = false, expirations = expirations) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }
}
