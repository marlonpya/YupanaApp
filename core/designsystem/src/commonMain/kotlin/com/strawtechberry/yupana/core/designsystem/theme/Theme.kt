package com.strawtechberry.yupana.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Tema raíz de Yupana. Dark-first (la app es oscura por defecto). Provee los tokens
 * propios vía CompositionLocals y alimenta MaterialTheme para los componentes M3.
 */
@Composable
fun YupanaTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalYupanaColors provides YupanaColoresOscuro,
        LocalYupanaTypography provides YupanaTypografiaDefault,
        LocalYupanaShapes provides YupanaShapesDefault,
        LocalYupanaSpacing provides YupanaSpacing(),
        LocalYupanaElevation provides YupanaElevation(),
    ) {
        MaterialTheme(
            colorScheme = YupanaDarkColorScheme,
            typography = YupanaMaterialTypography,
            shapes = YupanaMaterialShapes,
            content = content,
        )
    }
}

/** Punto de acceso a los tokens (espejo del patrón `MaterialTheme`). */
object YupanaTheme {
    val colors: YupanaColors
        @Composable @ReadOnlyComposable get() = LocalYupanaColors.current
    val typography: YupanaTypography
        @Composable @ReadOnlyComposable get() = LocalYupanaTypography.current
    val shapes: YupanaShapes
        @Composable @ReadOnlyComposable get() = LocalYupanaShapes.current
    val spacing: YupanaSpacing
        @Composable @ReadOnlyComposable get() = LocalYupanaSpacing.current
    val elevation: YupanaElevation
        @Composable @ReadOnlyComposable get() = LocalYupanaElevation.current
}
