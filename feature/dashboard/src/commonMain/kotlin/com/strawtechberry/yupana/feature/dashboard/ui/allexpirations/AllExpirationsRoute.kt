package com.strawtechberry.yupana.feature.dashboard.ui.allexpirations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/** "Todos los vencimientos" route: full expirations list with filters and search. */
@Composable
fun AllExpirationsRoute(
    onBack: () -> Unit,
    onOpenDetail: (String) -> Unit,
    viewModel: AllExpirationsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AllExpirationsEvent.NavigateBack -> onBack()
                is AllExpirationsEvent.NavigateToDetail -> onOpenDetail(event.assignmentId)
            }
        }
    }

    AllExpirationsScreen(state = state, onIntent = viewModel::onIntent)
}
