package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

data class YupanaNavItem(val label: String, val glyph: String)

/** 4 destinos del MVP (CLAUDE.md §6). Glyphs simples para no depender de una fuente de íconos. */
val yupanaNavItemsDefault = listOf(
    YupanaNavItem("Inicio", "⌂"),
    YupanaNavItem("Cuentas", "▤"),
    YupanaNavItem("Clientes", "☻"),
    YupanaNavItem("Ajustes", "⚙"),
)

@Composable
fun YupanaBottomNav(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<YupanaNavItem> = yupanaNavItemsDefault,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.superficie)
            .padding(vertical = spacing.sm),
    ) {
        items.forEachIndexed { index, item ->
            val active = index == selectedIndex
            val tint = if (active) colors.marca else colors.textoSecundario
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelect(index) }
                    .padding(vertical = spacing.xs),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = item.glyph, color = tint, style = YupanaTheme.typography.subtitle)
                Spacer(Modifier.height(spacing.xs))
                Text(text = item.label, color = tint, style = YupanaTheme.typography.caption)
            }
        }
    }
}
