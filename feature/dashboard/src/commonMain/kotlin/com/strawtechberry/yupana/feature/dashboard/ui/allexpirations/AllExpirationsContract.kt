package com.strawtechberry.yupana.feature.dashboard.ui.allexpirations

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

/** Filter buckets for "Todos los vencimientos", per days_left. */
enum class ExpirationFilter { Hoy, Proximos7Dias, EsteMes, Vencidos, Todos }

/** "Todos los vencimientos" screen state. */
data class AllExpirationsUiState(
    val expirations: List<UpcomingExpiration> = emptyList(),
    val filter: ExpirationFilter = ExpirationFilter.Todos,
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState {
    val visibleExpirations: List<UpcomingExpiration>
        get() = expirations
            .filter { matchesFilter(it.daysLeft, filter) }
            .filter { query.isBlank() || it.clientName.contains(query, ignoreCase = true) }
}

private fun matchesFilter(daysLeft: Int, filter: ExpirationFilter): Boolean = when (filter) {
    ExpirationFilter.Hoy -> daysLeft == 0
    ExpirationFilter.Proximos7Dias -> daysLeft in 0..7
    ExpirationFilter.EsteMes -> daysLeft in 0..30
    ExpirationFilter.Vencidos -> daysLeft < 0
    ExpirationFilter.Todos -> true
}

/** User actions on "Todos los vencimientos". */
sealed interface AllExpirationsIntent : UiIntent {
    data object Retry : AllExpirationsIntent
    data class FilterChanged(val filter: ExpirationFilter) : AllExpirationsIntent
    data class QueryChanged(val value: String) : AllExpirationsIntent
    data object BackClicked : AllExpirationsIntent
    data class ExpirationClicked(val assignmentId: String) : AllExpirationsIntent
}

/** One-time effects of "Todos los vencimientos". */
sealed interface AllExpirationsEvent : UiEvent {
    data object NavigateBack : AllExpirationsEvent
    data class NavigateToDetail(val assignmentId: String) : AllExpirationsEvent
}
