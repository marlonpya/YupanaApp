package com.strawtechberry.yupana.core.designsystem.state

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Skeleton con efecto shimmer (brillo pulsante) para estados de carga. */
@Composable
fun YupanaLoadingState(
    modifier: Modifier = Modifier,
    rows: Int = 3,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(900), repeatMode = RepeatMode.Reverse),
    )
    Column(
        modifier = modifier.fillMaxWidth().padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        repeat(rows) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(YupanaTheme.shapes.medium)
                    .background(colors.superficieElevada.copy(alpha = alpha)),
            )
        }
    }
}
