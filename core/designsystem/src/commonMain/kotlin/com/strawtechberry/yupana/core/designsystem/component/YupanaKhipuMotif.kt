package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Motivo khipu del header de login/registro: una barra degradada (terracota→dorado) de la que
 * cuelgan cordeles con nudos, evocando el instrumento contable andino que da nombre a Yupana.
 * Composable propio de marca; usa solo tokens del tema.
 */
@Composable
fun YupanaKhipuMotif(modifier: Modifier = Modifier) {
    val colors = YupanaTheme.colors
    // Cada cordel: alturas de tramo y color de cada nudo (marca/acento), de izquierda a derecha.
    val cordeles = listOf(
        Cordel(listOf(18.dp, 26.dp, 14.dp), listOf(colors.marca, colors.acento)),
        Cordel(listOf(30.dp, 20.dp, 18.dp), listOf(colors.acento, colors.marca)),
        Cordel(listOf(14.dp, 34.dp, 16.dp), listOf(colors.marca, colors.acento)),
        Cordel(listOf(24.dp, 22.dp), listOf(colors.marca)),
        Cordel(listOf(20.dp, 30.dp, 12.dp), listOf(colors.acento, colors.marca)),
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Barra maestra degradada de la que penden los cordeles.
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Brush.horizontalGradient(listOf(colors.marca, colors.acento))),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
            cordeles.forEach { CordelKhipu(it) }
        }
    }
}

private data class Cordel(val tramos: List<Dp>, val nudos: List<androidx.compose.ui.graphics.Color>)

@Composable
private fun CordelKhipu(cordel: Cordel) {
    val lineas = YupanaTheme.colors.lineas
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        cordel.tramos.forEachIndexed { index, tramo ->
            Box(Modifier.width(2.dp).height(tramo).background(lineas))
            // Entre tramos va un nudo; el último tramo queda como cola sin nudo.
            cordel.nudos.getOrNull(index)?.let { color ->
                val d = if (index == 0) 9.dp else 7.dp
                Box(Modifier.size(d).clip(CircleShape).background(color))
            }
        }
    }
}
