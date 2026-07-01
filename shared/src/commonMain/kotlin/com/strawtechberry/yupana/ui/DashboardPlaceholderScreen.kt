package com.strawtechberry.yupana.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaBrandLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Placeholder del Dashboard (Grupo 2 lo reemplaza). Sirve para validar el flujo de auth
 * end-to-end: llegar aquí tras login/registro y poder cerrar sesión para re-probar.
 */
@Composable
fun DashboardPlaceholderScreen(onCerrarSesion: () -> Unit) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Box(
        modifier = Modifier.fillMaxSize().background(colors.fondo).padding(spacing.xxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.lg),
        ) {
            YupanaBrandLogo()
            Text(
                text = "Dashboard (próximamente)",
                style = YupanaTheme.typography.title,
                color = colors.textoPrincipal,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Sesión iniciada. El tablero de próximos vencimientos llega en el siguiente grupo.",
                style = YupanaTheme.typography.body,
                color = colors.textoSecundario,
                textAlign = TextAlign.Center,
            )
            YupanaButton(
                text = "Cerrar sesión",
                onClick = onCerrarSesion,
                variant = YupanaButtonVariant.Secondary,
            )
        }
    }
}
