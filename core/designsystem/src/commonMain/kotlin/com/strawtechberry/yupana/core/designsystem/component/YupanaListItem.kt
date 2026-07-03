package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Fila de lista para pantallas de ajustes: icono opcional, título/subtítulo y trailing (chevron por defecto). */
@Composable
fun YupanaListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color? = null,
    trailing: @Composable (RowScope.() -> Unit)? = { DefaultChevron() },
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Text(text = icon, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
            Spacer(Modifier.width(spacing.md))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = YupanaTheme.typography.body, color = titleColor ?: colors.textoPrincipal)
            if (subtitle != null) {
                Text(text = subtitle, style = YupanaTheme.typography.caption, color = colors.textoSecundario)
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(spacing.sm))
            trailing()
        }
    }
}

@Composable
private fun DefaultChevron() {
    Text(text = "›", style = YupanaTheme.typography.title, color = YupanaTheme.colors.textoSecundario)
}

@Preview
@Composable
private fun YupanaListItemPreview() {
    Column {
        YupanaListItem(icon = "👤", title = "Mi cuenta", subtitle = "marlon@correo.com", onClick = {})
        YupanaListItem(title = "Cerrar sesión", titleColor = YupanaTheme.colors.peligro, trailing = null, onClick = {})
    }
}
