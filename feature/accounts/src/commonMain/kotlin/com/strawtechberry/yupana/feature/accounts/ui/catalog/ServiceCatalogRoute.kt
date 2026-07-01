package com.strawtechberry.yupana.feature.accounts.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Service catalog route. Used both as a standalone browser (from Ajustes/Mis cuentas)
 * and as a picker invoked from the account form — the caller decides what
 * [onServiceSelected] does with the chosen id; this Route/Screen don't know which mode
 * they're in beyond [picker] affecting the title/copy.
 */
@Composable
fun ServiceCatalogRoute(
    picker: Boolean,
    onBack: () -> Unit,
    onServiceSelected: (String) -> Unit,
    viewModel: ServiceCatalogViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ServiceCatalogEvent.ServiceSelected -> onServiceSelected(event.id)
            }
        }
    }

    ServiceCatalogScreen(picker = picker, state = state, onIntent = viewModel::onIntent, onBack = onBack)
}
