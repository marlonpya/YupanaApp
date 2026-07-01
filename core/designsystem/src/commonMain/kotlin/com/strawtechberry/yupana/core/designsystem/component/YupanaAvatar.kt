package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Avatar circular con gradiente de marca terracota→dorado y la inicial. */
@Composable
fun YupanaAvatar(
    initial: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    val colors = YupanaTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(colors.marca, colors.acento))),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = initial, color = colors.onMarca, style = YupanaTheme.typography.subtitle)
    }
}
