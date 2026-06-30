package com.strawtechberry.yupana

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tokens de marca "oscuro andino" (CLAUDE.md §5). Aquí inline solo para el hello-world;
// pasarán al design system (:core:designsystem) en Fase 2.
private val Fondo = Color(0xFF15110E)
private val Terracota = Color(0xFFC8643C)

/**
 * Punto de entrada COMPARTIDO (Android + iOS). No es el design system: valida que
 * Compose Multiplatform renderiza en ambas plataformas.
 */
@Composable
fun App() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = "Yupana",
            style = TextStyle(
                color = Terracota,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}
