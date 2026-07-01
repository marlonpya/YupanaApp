package com.strawtechberry.yupana.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class YupanaShapes(
    val small: RoundedCornerShape,
    val medium: RoundedCornerShape,
    val large: RoundedCornerShape,
    val pill: RoundedCornerShape,
)

// CLAUDE.md §5: "Esquinas redondeadas generosas (tarjetas ~18px)".
val YupanaShapesDefault = YupanaShapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    pill = RoundedCornerShape(percent = 50),
)

val LocalYupanaShapes = staticCompositionLocalOf { YupanaShapesDefault }

val YupanaMaterialShapes = Shapes(
    small = YupanaShapesDefault.small,
    medium = YupanaShapesDefault.medium,
    large = YupanaShapesDefault.large,
)
