package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Marca de Yupana: tile con degradado terracota→dorado y la "Y", junto al wordmark "Yupana".
 * [tileSize] controla el tamaño (login grande, headers pequeños). Si [showWordmark] es false
 * se muestra solo el tile.
 */
@Composable
fun YupanaBrandLogo(
    modifier: Modifier = Modifier,
    tileSize: Dp = 48.dp,
    wordmarkSize: TextUnit = 38.sp,
    showWordmark: Boolean = true,
) {
    val colors = YupanaTheme.colors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Box(
            modifier = Modifier
                .size(tileSize)
                .clip(RoundedCornerShape(tileSize / 3.4f))
                .background(Brush.linearGradient(listOf(colors.marca, colors.acento))),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Y",
                color = colors.onMarca,
                fontWeight = FontWeight.SemiBold,
                fontSize = (tileSize.value * 0.55f).sp,
            )
        }
        if (showWordmark) {
            Text(
                text = "Yupana",
                color = colors.textoPrincipal,
                fontWeight = FontWeight.SemiBold,
                fontSize = wordmarkSize,
            )
        }
    }
}
