package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Urgencia de vencimiento (CLAUDE.md §5): hoy=peligro, pronto=alerta, al día=éxito. */
enum class YupanaEstado { Hoy, Pronto, AlDia }

@Composable
fun colorDeEstado(estado: YupanaEstado): Color = when (estado) {
    YupanaEstado.Hoy -> YupanaTheme.colors.peligro
    YupanaEstado.Pronto -> YupanaTheme.colors.alerta
    YupanaEstado.AlDia -> YupanaTheme.colors.exito
}

@Composable
fun YupanaChip(
    text: String,
    estado: YupanaEstado,
    modifier: Modifier = Modifier,
) {
    val color = colorDeEstado(estado)
    Text(
        text = text,
        style = YupanaTheme.typography.caption,
        color = color,
        modifier = modifier
            .clip(YupanaTheme.shapes.pill)
            .background(color.copy(alpha = 0.16f))
            .padding(horizontal = YupanaTheme.spacing.md, vertical = YupanaTheme.spacing.xs),
    )
}
