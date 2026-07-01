package com.strawtechberry.yupana.feature.clients.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Clients list route: connects the [ClientsListViewModel] (Koin) with the UI and
 * translates MVI events into navigation. Reloads on every re-entry into composition
 * (e.g. after popping back from the form) since [ClientsListViewModel] is retained
 * across that navigation.
 */
@Composable
fun ClientsListRoute(
    onCreateClient: () -> Unit,
    onEditClient: (String) -> Unit,
    viewModel: ClientsListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ClientsListEvent.NavigateToCreate -> onCreateClient()
                is ClientsListEvent.NavigateToEdit -> onEditClient(event.id)
            }
        }
    }

    ClientsListScreen(state = state, onIntent = viewModel::onIntent)
}
