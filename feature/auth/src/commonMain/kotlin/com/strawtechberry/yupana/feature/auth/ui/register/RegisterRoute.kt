package com.strawtechberry.yupana.feature.auth.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/** Route de Registro: conecta el [RegisterViewModel] con la UI y mapea eventos a navegación. */
@Composable
fun RegisterRoute(
    onRegistroExitoso: () -> Unit,
    onIrALogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { evento ->
            when (evento) {
                RegisterEvent.NavegarADashboard -> onRegistroExitoso()
                RegisterEvent.NavegarALogin -> onIrALogin()
            }
        }
    }

    RegisterScreen(state = state, onIntent = viewModel::onIntent)
}
