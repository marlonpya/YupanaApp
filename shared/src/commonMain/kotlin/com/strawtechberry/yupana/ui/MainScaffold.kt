package com.strawtechberry.yupana.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaBottomNav
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.ui.list.AccountsListRoute
import com.strawtechberry.yupana.feature.auth.domain.usecase.SignOutUseCase
import com.strawtechberry.yupana.feature.clients.ui.list.ClientsListRoute
import com.strawtechberry.yupana.feature.dashboard.ui.dashboard.DashboardRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Shell con el bottom nav de 4 pestañas (Inicio/Cuentas/Clientes/Ajustes). El estado
 * de pestaña es local (sin NavHost anidado); solo los formularios de cliente/cuenta, el
 * catálogo de servicios y el detalle de asignación navegan de verdad, vía el
 * NavController de nivel superior (callbacks recibidos). El FAB de "Nueva asignación"
 * solo se muestra en la pestaña Inicio.
 */
@Composable
fun MainScaffold(
    onCerrarSesion: () -> Unit,
    onCreateClient: () -> Unit,
    onOpenClientDetail: (String) -> Unit,
    onCreateAccount: () -> Unit,
    onOpenAccountDetail: (String) -> Unit,
    onOpenServiceCatalog: () -> Unit,
    onCreateAssignment: () -> Unit,
    onOpenAssignmentDetail: (String) -> Unit,
    onOpenAllExpirations: () -> Unit,
    signOut: SignOutUseCase = koinInject(),
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val handleSignOut: () -> Unit = {
        scope.launch {
            signOut()
            onCerrarSesion()
        }
    }

    Scaffold(
        bottomBar = { YupanaBottomNav(selectedIndex = selectedIndex, onSelect = { selectedIndex = it }) },
        floatingActionButton = {
            if (selectedIndex == 0) {
                FloatingActionButton(
                    onClick = onCreateAssignment,
                    containerColor = YupanaTheme.colors.marca,
                    contentColor = YupanaTheme.colors.onMarca,
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Nueva asignación")
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedIndex) {
                0 -> DashboardRoute(
                    onOpenDetail = onOpenAssignmentDetail,
                    onCreateAssignment = onCreateAssignment,
                    onOpenAllExpirations = onOpenAllExpirations,
                )
                1 -> AccountsListRoute(
                    onCreateAccount = onCreateAccount,
                    onOpenAccountDetail = onOpenAccountDetail,
                    onOpenServiceCatalog = onOpenServiceCatalog,
                    onCreateAssignment = onCreateAssignment,
                )
                2 -> ClientsListRoute(onCreateClient = onCreateClient, onOpenClientDetail = onOpenClientDetail)
                else -> SettingsPlaceholderScreen(onOpenServiceCatalog = onOpenServiceCatalog, onCerrarSesion = handleSignOut)
            }
        }
    }
}
