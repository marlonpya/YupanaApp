package com.strawtechberry.yupana.core.designsystem.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Estado vacío: glifo + título + mensaje + acción opcional. */
@Composable
fun YupanaEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    glyph: String = "∅",
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Column(
        modifier = modifier.fillMaxWidth().padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape).background(colors.superficieElevada),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = glyph, style = YupanaTheme.typography.title, color = colors.textoSecundario)
        }
        Text(text = title, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
        Text(
            text = message,
            style = YupanaTheme.typography.body,
            color = colors.textoSecundario,
            textAlign = TextAlign.Center,
        )
        if (actionText != null && onAction != null) {
            YupanaButton(text = actionText, onClick = onAction)
        }
    }
}
