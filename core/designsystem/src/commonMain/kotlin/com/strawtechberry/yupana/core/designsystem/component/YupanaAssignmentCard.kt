package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Item de la lista "Próximos a vencer": barra de urgencia, logo, cliente, precio y chip. */
@Composable
fun YupanaAssignmentCard(
    cliente: String,
    subtitulo: String,
    precio: String,
    estado: YupanaEstado,
    estadoLabel: String,
    logoText: String,
    logoColor: Color,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val shape = YupanaTheme.shapes.medium
    val barColor = colorDeEstado(estado)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.superficie)
            .border(YupanaTheme.elevation.bordeGrosor, colors.lineas, shape)
            .padding(spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            Modifier
                .width(4.dp)
                .height(46.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(barColor),
        )
        Spacer(Modifier.width(spacing.md))
        YupanaServiceLogo(text = logoText, color = logoColor)
        Spacer(Modifier.width(spacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cliente, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
            Text(text = subtitulo, style = YupanaTheme.typography.label, color = colors.textoSecundario)
        }
        Spacer(Modifier.width(spacing.md))
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "S/.$precio", style = YupanaTheme.typography.price, color = colors.acento)
            Spacer(Modifier.height(spacing.sm))
            YupanaChip(text = estadoLabel, estado = estado)
        }
    }
}
