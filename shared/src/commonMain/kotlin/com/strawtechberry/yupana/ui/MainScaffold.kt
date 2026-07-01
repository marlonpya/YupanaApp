package com.strawtechberry.yupana.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaBottomNav
import com.strawtechberry.yupana.feature.accounts.ui.list.AccountsListRoute
import com.strawtechberry.yupana.feature.clients.ui.list.ClientsListRoute

/**
 * Shell con el bottom nav de 4 pestañas (Inicio/Cuentas/Clientes/Ajustes). El estado
 * de pestaña es local (sin NavHost anidado); solo los formularios de cliente/cuenta y
 * el catálogo de servicios navegan de verdad, vía el NavController de nivel superior
 * (callbacks recibidos).
 */
@Composable
fun MainScaffold(
    onCerrarSesion: () -> Unit,
    onCreateClient: () -> Unit,
    onEditClient: (String) -> Unit,
    onCreateAccount: () -> Unit,
    onOpenAccountDetail: (String) -> Unit,
    onOpenServiceCatalog: () -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = { YupanaBottomNav(selectedIndex = selectedIndex, onSelect = { selectedIndex = it }) },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedIndex) {
                0 -> DashboardPlaceholderRoute(onCerrarSesion = onCerrarSesion)
                1 -> AccountsListRoute(
                    onCreateAccount = onCreateAccount,
                    onOpenAccountDetail = onOpenAccountDetail,
                    onOpenServiceCatalog = onOpenServiceCatalog,
                )
                2 -> ClientsListRoute(onCreateClient = onCreateClient, onEditClient = onEditClient)
                else -> SettingsPlaceholderScreen(onOpenServiceCatalog = onOpenServiceCatalog)
            }
        }
    }
}
