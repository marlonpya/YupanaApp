package com.strawtechberry.yupana.feature.dashboard.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AssignmentDetailRoute(
    assignmentId: String,
    onBack: () -> Unit,
    onActionCompleted: () -> Unit,
    viewModel: AssignmentDetailViewModel = koinViewModel(key = assignmentId) { parametersOf(assignmentId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AssignmentDetailEvent.NavigateBack -> onBack()
                AssignmentDetailEvent.ActionCompleted -> onActionCompleted()
            }
        }
    }

    AssignmentDetailScreen(state = state, onIntent = viewModel::onIntent)
}
