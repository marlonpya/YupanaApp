package com.strawtechberry.yupana.feature.assignment.ui.move

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/** "Mover integrante" route: destination account + free profile picker for one assignment. */
@Composable
fun MoveMemberRoute(
    assignmentId: String,
    onBack: () -> Unit,
    onMoved: () -> Unit,
    onCreateAccount: () -> Unit,
    viewModel: MoveMemberViewModel = koinViewModel(key = assignmentId) { parametersOf(assignmentId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MoveMemberEvent.NavigateBack -> onBack()
                MoveMemberEvent.MoveCompleted -> onMoved()
                MoveMemberEvent.NavigateToCreateAccount -> onCreateAccount()
            }
        }
    }

    MoveMemberScreen(state = state, onIntent = viewModel::onIntent)
}
