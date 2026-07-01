package com.strawtechberry.yupana.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Niveles de elevación y grosor de borde estándar del design system. */
@Immutable
data class YupanaElevation(
    val none: Dp = 0.dp,
    val low: Dp = 1.dp,
    val medium: Dp = 3.dp,
    val high: Dp = 6.dp,
    val bordeGrosor: Dp = 1.dp,
)

val LocalYupanaElevation = staticCompositionLocalOf { YupanaElevation() }
