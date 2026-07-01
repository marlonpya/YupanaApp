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
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Placeholder de "Mis cuentas" (grupo posterior lo reemplaza). */
@Composable
fun AccountsPlaceholderScreen() {
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
                text = "Mis cuentas (próximamente)",
                style = YupanaTheme.typography.title,
                color = colors.textoPrincipal,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Aquí verás tus cuentas agrupadas por servicio en un grupo posterior.",
                style = YupanaTheme.typography.body,
                color = colors.textoSecundario,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun AccountsPlaceholderScreenPreview() {
    AccountsPlaceholderScreen()
}
