package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

enum class YupanaButtonVariant { Primary, Secondary, Text, Destructive }

/**
 * Botón principal de Yupana. Soporta estado [loading]: muestra un spinner y bloquea el clic
 * mientras dura una operación (login/registro).
 */
@Composable
fun YupanaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: YupanaButtonVariant = YupanaButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val colors = YupanaTheme.colors
    val shapes = YupanaTheme.shapes
    val spacing = YupanaTheme.spacing
    val interactuable = enabled && !loading
    val alpha = if (interactuable) 1f else 0.4f

    val container: Color
    val foreground: Color
    val borderColor: Color?
    when (variant) {
        YupanaButtonVariant.Primary -> { container = colors.marca; foreground = colors.onMarca; borderColor = null }
        YupanaButtonVariant.Secondary -> { container = colors.superficieElevada; foreground = colors.textoPrincipal; borderColor = colors.lineas }
        YupanaButtonVariant.Text -> { container = Color.Transparent; foreground = colors.marca; borderColor = null }
        YupanaButtonVariant.Destructive -> { container = colors.peligro; foreground = colors.onMarca; borderColor = null }
    }

    Box(
        modifier = modifier
            .clip(shapes.pill)
            .then(
                if (borderColor != null) Modifier.border(YupanaTheme.elevation.bordeGrosor, borderColor.copy(alpha = alpha), shapes.pill)
                else Modifier
            )
            .background(container.copy(alpha = if (container == Color.Transparent) 0f else alpha))
            .clickable(enabled = interactuable, onClick = onClick)
            .padding(horizontal = spacing.xl, vertical = spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = foreground,
                strokeWidth = 2.dp,
            )
        } else {
            androidx.compose.material3.Text(
                text = text,
                color = foreground.copy(alpha = alpha),
                style = YupanaTheme.typography.label.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}
