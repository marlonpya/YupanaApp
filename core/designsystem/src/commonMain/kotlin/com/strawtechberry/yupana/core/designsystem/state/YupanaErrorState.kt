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
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Estado de error con botón "Reintentar". */
@Composable
fun YupanaErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Algo salió mal",
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Column(
        modifier = modifier.fillMaxWidth().padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape).background(colors.peligro.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "!", style = YupanaTheme.typography.display, color = colors.peligro)
        }
        Text(text = title, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
        Text(
            text = message,
            style = YupanaTheme.typography.body,
            color = colors.textoSecundario,
            textAlign = TextAlign.Center,
        )
        YupanaButton(text = "Reintentar", onClick = onRetry, variant = YupanaButtonVariant.Secondary)
    }
}
