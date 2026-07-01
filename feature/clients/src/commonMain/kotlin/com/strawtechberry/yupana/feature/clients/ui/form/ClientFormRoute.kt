package com.strawtechberry.yupana.feature.clients.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Client form route: connects the [ClientFormViewModel] with the UI. [clientId] is
 * `null` in create mode, or the id of the client being edited.
 */
@Composable
fun ClientFormRoute(
    clientId: String?,
    onSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: ClientFormViewModel = koinViewModel(key = clientId) { parametersOf(clientId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ClientFormEvent.NavigateBack -> onBack()
                ClientFormEvent.SavedSuccessfully -> onSaved()
            }
        }
    }

    ClientFormScreen(state = state, onIntent = viewModel::onIntent)
}
