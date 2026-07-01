package com.strawtechberry.yupana.feature.auth.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel

/** Route del Splash: observa la sesión (vía ViewModel) y navega al destino resuelto. */
@Composable
fun SplashRoute(
    onSesionActiva: () -> Unit,
    onSinSesion: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { evento ->
            when (evento) {
                SplashEvent.NavegarADashboard -> onSesionActiva()
                SplashEvent.NavegarALogin -> onSinSesion()
            }
        }
    }

    SplashScreen()
}
