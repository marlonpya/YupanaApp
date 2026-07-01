package com.strawtechberry.yupana.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.strawtechberry.yupana.feature.auth.domain.usecase.SignOutUseCase
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/** Route del placeholder: cierra la sesión (Koin) y avisa para navegar de vuelta al Login. */
@Composable
fun DashboardPlaceholderRoute(
    onCerrarSesion: () -> Unit,
    cerrarSesion: SignOutUseCase = koinInject(),
) {
    val scope = rememberCoroutineScope()
    DashboardPlaceholderScreen(
        onCerrarSesion = {
            scope.launch {
                cerrarSesion()
                onCerrarSesion()
            }
        },
    )
}
