package com.strawtechberry.yupana

import androidx.compose.runtime.Composable
import com.strawtechberry.yupana.core.designsystem.showcase.DesignSystemShowcase
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Punto de entrada COMPARTIDO (Android + iOS). Fase 2: renderiza el catálogo del design
 * system bajo el tema oscuro andino. Las pantallas reales del MVP llegan en Fase 3.
 */
@Composable
fun App() {
    YupanaTheme {
        DesignSystemShowcase()
    }
}
