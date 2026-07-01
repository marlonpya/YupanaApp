package com.strawtechberry.yupana.feature.auth.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Contenedor común de las pantallas de auth: fondo oscuro con un glow terracota sutil arriba
 * (adorno local, no toca los tokens) y una columna centrada y desplazable.
 */
@Composable
fun AuthScreenContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.fondo)
            .background(
                Brush.verticalGradient(
                    colors = listOf(colors.marca.copy(alpha = 0.10f), Color.Transparent),
                    endY = 700f,
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = spacing.xxl, vertical = spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            content = content,
        )
    }
}
