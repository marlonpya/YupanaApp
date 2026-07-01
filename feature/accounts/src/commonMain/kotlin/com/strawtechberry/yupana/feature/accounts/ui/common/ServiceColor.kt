package com.strawtechberry.yupana.feature.accounts.ui.common

import androidx.compose.ui.graphics.Color

/** Parses a `"#RRGGBB"` hex string, or `null` if blank/invalid — caller picks the fallback. */
fun parseServiceColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    return try {
        val clean = hex.removePrefix("#")
        val withAlpha = if (clean.length == 6) "FF$clean" else clean
        Color(withAlpha.toLong(16))
    } catch (e: NumberFormatException) {
        null
    }
}

/** Preset swatches offered when creating a custom service (no color picker in the design system). */
val servicePresetColors = listOf("#E5594E", "#6FBF8E", "#4E8FE5", "#E0A458", "#8E6FE5", "#5EC6C1")
