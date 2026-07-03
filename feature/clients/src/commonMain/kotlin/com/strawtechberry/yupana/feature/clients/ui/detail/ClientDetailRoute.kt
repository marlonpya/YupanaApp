package com.strawtechberry.yupana.feature.clients.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/** Client detail route: shows one client and its subscriptions (active + history). */
@Composable
fun ClientDetailRoute(
    clientId: String,
    onBack: () -> Unit,
    onEditClient: (String) -> Unit,
    viewModel: ClientDetailViewModel = koinViewModel(key = clientId) { parametersOf(clientId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ClientDetailEvent.NavigateBack -> onBack()
                is ClientDetailEvent.NavigateToEdit -> onEditClient(event.clientId)
            }
        }
    }

    ClientDetailScreen(state = state, onIntent = viewModel::onIntent)
}
