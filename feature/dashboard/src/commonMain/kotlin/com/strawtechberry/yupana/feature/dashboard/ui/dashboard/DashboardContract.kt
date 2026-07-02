package com.strawtechberry.yupana.feature.dashboard.ui.dashboard

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration

/** Dashboard state: all active assignments plus derived urgency metrics. */
data class DashboardUiState(
    val expirations: List<UpcomingExpiration> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState {
    val dueTodayCount: Int get() = expirations.count { it.daysLeft <= 0 }
    val dueSoonCount: Int get() = expirations.count { it.daysLeft in 1..7 }
    val amountToCollect: Double get() = expirations.filter { it.daysLeft <= 7 }.sumOf { it.priceCharged ?: 0.0 }
}

/** User actions on the dashboard. */
sealed interface DashboardIntent : UiIntent {
    data object Retry : DashboardIntent
    data class ExpirationClicked(val assignmentId: String) : DashboardIntent
    data object NewAssignmentClicked : DashboardIntent
}

/** One-time effects of the dashboard. */
sealed interface DashboardEvent : UiEvent {
    data class NavigateToDetail(val assignmentId: String) : DashboardEvent
    data object NavigateToNewAssignment : DashboardEvent
}
