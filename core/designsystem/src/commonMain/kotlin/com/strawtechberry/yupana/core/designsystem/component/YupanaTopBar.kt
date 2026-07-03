package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

@Composable
fun YupanaTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = spacing.lg, vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            Text(
                text = "‹",
                style = YupanaTheme.typography.display,
                color = colors.textoPrincipal,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(end = spacing.md),
            )
        }
        Text(
            text = title,
            style = YupanaTheme.typography.title,
            color = colors.textoPrincipal,
            modifier = Modifier.weight(1f),
        )
        action?.invoke()
    }
}
