package com.strawtechberry.yupana.feature.auth.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route de Login: conecta el [LoginViewModel] (Koin) con la UI y traduce los eventos MVI en
 * navegación. El NavController vive solo aquí (vía callbacks), no en el ViewModel ni el Screen.
 */
@Composable
fun LoginRoute(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    onOlvidePassword: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { evento ->
            when (evento) {
                LoginEvent.NavegarADashboard -> onLoginExitoso()
                LoginEvent.NavegarARegistro -> onIrARegistro()
                LoginEvent.NavegarAOlvidePassword -> onOlvidePassword()
            }
        }
    }

    LoginScreen(state = state, onIntent = viewModel::onIntent)
}
