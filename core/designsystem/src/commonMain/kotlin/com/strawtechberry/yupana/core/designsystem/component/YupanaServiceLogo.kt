package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Cuadro con la inicial del servicio (Netflix "N", Spotify "S"...) sobre su color de marca. */
@Composable
fun YupanaServiceLogo(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(YupanaTheme.shapes.small)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = Color.White, style = YupanaTheme.typography.subtitle)
    }
}
