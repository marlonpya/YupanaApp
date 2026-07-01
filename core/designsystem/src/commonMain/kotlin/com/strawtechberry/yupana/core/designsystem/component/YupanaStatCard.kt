package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Métrica del dashboard: número grande sobre un label corto. */
@Composable
fun YupanaStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = YupanaTheme.colors.acento,
) {
    YupanaCard(modifier = modifier, padding = YupanaTheme.spacing.md) {
        Text(text = label, style = YupanaTheme.typography.caption, color = YupanaTheme.colors.textoSecundario)
        Spacer(Modifier.height(YupanaTheme.spacing.sm))
        Text(text = value, style = YupanaTheme.typography.display, color = valueColor)
    }
}
