package com.strawtechberry.yupana.feature.dashboard.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Dashboard route: reloads on every re-entry into composition (e.g. after acting on
 * an assignment from the detail screen), same pattern as `AccountsListRoute`.
 */
@Composable
fun DashboardRoute(
    onOpenDetail: (String) -> Unit,
    onCreateAssignment: () -> Unit,
    onOpenAllExpirations: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DashboardEvent.NavigateToDetail -> onOpenDetail(event.assignmentId)
                DashboardEvent.NavigateToNewAssignment -> onCreateAssignment()
                DashboardEvent.NavigateToAllExpirations -> onOpenAllExpirations()
            }
        }
    }

    DashboardScreen(state = state, onIntent = viewModel::onIntent)
}
