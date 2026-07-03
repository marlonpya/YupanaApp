package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Chip de filtro/tab (seleccionado/no-seleccionado), distinto de [YupanaChip] (solo urgencia). */
@Composable
fun YupanaFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    val shape = YupanaTheme.shapes.pill

    Text(
        text = text,
        style = YupanaTheme.typography.label,
        color = if (selected) colors.onMarca else colors.textoSecundario,
        modifier = modifier
            .clip(shape)
            .let { if (selected) it.background(colors.marca) else it.border(YupanaTheme.elevation.bordeGrosor, colors.lineas, shape) }
            .clickable(onClick = onClick)
            .padding(horizontal = YupanaTheme.spacing.md, vertical = YupanaTheme.spacing.sm),
    )
}
