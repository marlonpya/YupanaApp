package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

@Composable
fun YupanaCard(
    modifier: Modifier = Modifier,
    padding: Dp = YupanaTheme.spacing.lg,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = YupanaTheme.colors
    val shape = YupanaTheme.shapes.medium
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.superficie)
            .border(YupanaTheme.elevation.bordeGrosor, colors.lineas, shape)
            .padding(padding),
        content = content,
    )
}
