package com.strawtechberry.yupana.feature.accounts.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Profile-occupancy bar: green when comfortable, amber near capacity, red when full.
 * No such component exists in `:core:designsystem` yet — kept local since it's only
 * used here, mirroring the project's bias against premature abstraction.
 */
@Composable
fun OccupancyBar(occupied: Int, total: Int, modifier: Modifier = Modifier) {
    val colors = YupanaTheme.colors
    val ratio = if (total == 0) 0f else occupied.toFloat() / total
    val barColor = when {
        total > 0 && occupied >= total -> colors.peligro
        ratio >= 0.75f -> colors.alerta
        else -> colors.exito
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(colors.lineas),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(50))
                    .background(barColor),
            )
        }
        Spacer(Modifier.width(YupanaTheme.spacing.sm))
        Text("$occupied/$total", style = YupanaTheme.typography.caption, color = colors.textoSecundario)
    }
}
