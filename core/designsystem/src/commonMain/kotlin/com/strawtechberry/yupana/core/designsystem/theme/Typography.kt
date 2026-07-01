package com.strawtechberry.yupana.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// CLAUDE.md §5: "Tipografía Roboto/Roboto Flex". En Android FontFamily.Default ES Roboto.
// Bundlear Roboto Flex variable vía composeResources es un refinamiento cross-platform pendiente.
private val Familia = FontFamily.Default

@Immutable
data class YupanaTypography(
    val display: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val price: TextStyle,
)

val YupanaTypografiaDefault = YupanaTypography(
    display = TextStyle(fontFamily = Familia, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, lineHeight = 36.sp),
    title = TextStyle(fontFamily = Familia, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    subtitle = TextStyle(fontFamily = Familia, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp),
    body = TextStyle(fontFamily = Familia, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 20.sp),
    label = TextStyle(fontFamily = Familia, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 16.sp),
    caption = TextStyle(fontFamily = Familia, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp),
    price = TextStyle(fontFamily = Familia, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 18.sp),
)

val LocalYupanaTypography = staticCompositionLocalOf { YupanaTypografiaDefault }

/** Mapeo a M3 Typography para componentes Material. */
val YupanaMaterialTypography = Typography(
    headlineMedium = YupanaTypografiaDefault.display,
    titleLarge = YupanaTypografiaDefault.title,
    titleMedium = YupanaTypografiaDefault.subtitle,
    bodyMedium = YupanaTypografiaDefault.body,
    labelMedium = YupanaTypografiaDefault.label,
    labelSmall = YupanaTypografiaDefault.caption,
)
