package com.strawtechberry.yupana.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Valores EXACTOS de CLAUDE.md §5 ("oscuro andino"). Única fuente de los hex. */
internal object YupanaPalette {
    val Fondo = Color(0xFF15110E)
    val Superficie = Color(0xFF1E1813)
    val SuperficieElevada = Color(0xFF271F18)
    val Terracota = Color(0xFFC8643C)
    val Dorado = Color(0xFFE0A458)
    val TextoPrincipal = Color(0xFFF3E9DF)
    val TextoSecundario = Color(0xFFB8A793)
    val Lineas = Color(0xFF322820)
    val Exito = Color(0xFF6FBF8E)
    val Alerta = Color(0xFFE0A458)
    val Peligro = Color(0xFFE5594E)

    /** Texto/íconos sobre superficies de marca/dorado/peligro. */
    val OnOscuro = Color(0xFF15110E)
}

@Immutable
data class YupanaColors(
    val fondo: Color,
    val superficie: Color,
    val superficieElevada: Color,
    val marca: Color,
    val acento: Color,
    val textoPrincipal: Color,
    val textoSecundario: Color,
    val lineas: Color,
    val exito: Color,
    val alerta: Color,
    val peligro: Color,
    val onMarca: Color,
)

val YupanaColoresOscuro = YupanaColors(
    fondo = YupanaPalette.Fondo,
    superficie = YupanaPalette.Superficie,
    superficieElevada = YupanaPalette.SuperficieElevada,
    marca = YupanaPalette.Terracota,
    acento = YupanaPalette.Dorado,
    textoPrincipal = YupanaPalette.TextoPrincipal,
    textoSecundario = YupanaPalette.TextoSecundario,
    lineas = YupanaPalette.Lineas,
    exito = YupanaPalette.Exito,
    alerta = YupanaPalette.Alerta,
    peligro = YupanaPalette.Peligro,
    onMarca = YupanaPalette.OnOscuro,
)

val LocalYupanaColors = staticCompositionLocalOf { YupanaColoresOscuro }

/** Mapeo a Material 3 para que los componentes M3 hereden la paleta. */
val YupanaDarkColorScheme = darkColorScheme(
    primary = YupanaPalette.Terracota,
    onPrimary = YupanaPalette.OnOscuro,
    secondary = YupanaPalette.Dorado,
    onSecondary = YupanaPalette.OnOscuro,
    background = YupanaPalette.Fondo,
    onBackground = YupanaPalette.TextoPrincipal,
    surface = YupanaPalette.Superficie,
    onSurface = YupanaPalette.TextoPrincipal,
    surfaceVariant = YupanaPalette.SuperficieElevada,
    onSurfaceVariant = YupanaPalette.TextoSecundario,
    outline = YupanaPalette.Lineas,
    error = YupanaPalette.Peligro,
    onError = YupanaPalette.OnOscuro,
)
