package com.strawtechberry.yupana.feature.settings.ui.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaListItem
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsRootScreen(
    state: SettingsRootUiState,
    onIntent: (SettingsRootIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(title = "Ajustes")

        Column(
            modifier = Modifier.fillMaxSize().padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.lg),
        ) {
            SettingsSection(title = "Cuenta") {
                YupanaListItem(
                    icon = "👤",
                    title = "Mi cuenta",
                    onClick = { onIntent(SettingsRootIntent.MyAccountClicked) },
                )
                YupanaListItem(
                    icon = "🚪",
                    title = "Cerrar sesión",
                    titleColor = colors.peligro,
                    trailing = null,
                    onClick = { onIntent(SettingsRootIntent.LogoutClicked) },
                )
            }

            SettingsSection(title = "Notificaciones") {
                YupanaListItem(
                    icon = "🔔",
                    title = "Preferencias de notificación",
                    onClick = { onIntent(SettingsRootIntent.NotificationPreferencesClicked) },
                )
            }

            SettingsSection(title = "General") {
                YupanaListItem(
                    icon = "📺",
                    title = "Catálogo de servicios",
                    onClick = { onIntent(SettingsRootIntent.ServiceCatalogClicked) },
                )
            }
        }
    }

    if (state.showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { onIntent(SettingsRootIntent.DismissLogoutConfirm) },
            title = { Text("Cerrar sesión", color = colors.textoPrincipal) },
            text = { Text("¿Seguro que quieres cerrar sesión?", color = colors.textoSecundario) },
            confirmButton = {
                YupanaButton(
                    text = "Cerrar sesión",
                    variant = YupanaButtonVariant.Destructive,
                    onClick = { onIntent(SettingsRootIntent.ConfirmLogout) },
                )
            },
            dismissButton = {
                YupanaButton(
                    text = "Cancelar",
                    variant = YupanaButtonVariant.Text,
                    onClick = { onIntent(SettingsRootIntent.DismissLogoutConfirm) },
                )
            },
        )
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    val spacing = YupanaTheme.spacing
    Column {
        Text(
            text = title,
            style = YupanaTheme.typography.label,
            color = YupanaTheme.colors.textoSecundario,
            modifier = Modifier.padding(bottom = spacing.sm),
        )
        YupanaCard(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Preview
@Composable
private fun SettingsRootScreenPreview() {
    SettingsRootScreen(state = SettingsRootUiState(), onIntent = {})
}
