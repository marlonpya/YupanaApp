package com.strawtechberry.yupana.core.designsystem.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaAssignmentCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaAvatar
import com.strawtechberry.yupana.core.designsystem.component.YupanaBottomNav
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaChip
import com.strawtechberry.yupana.core.designsystem.component.YupanaEstado
import com.strawtechberry.yupana.core.designsystem.component.YupanaServiceLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaStatCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Catálogo que renderiza todos los tokens y componentes del design system para QA visual. */
@Composable
fun DesignSystemShowcase(modifier: Modifier = Modifier) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.fondo)
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        Text("Yupana · Design System", style = YupanaTheme.typography.display, color = colors.textoPrincipal)
        Text("oscuro andino", style = YupanaTheme.typography.body, color = colors.textoSecundario)

        Seccion("Colores") { ColoresShowcase() }
        Seccion("Tipografía") { TipografiaShowcase() }
        Seccion("Espaciado") { EspaciadoShowcase() }

        Seccion("Botones") {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                YupanaButton("Primario", {}, variant = YupanaButtonVariant.Primary)
                YupanaButton("Secundario", {}, variant = YupanaButtonVariant.Secondary)
                YupanaButton("Texto", {}, variant = YupanaButtonVariant.Text)
                YupanaButton("Destructivo", {}, variant = YupanaButtonVariant.Destructive)
                YupanaButton("Deshabilitado", {}, enabled = false)
            }
        }

        Seccion("Campos") {
            var email by remember { mutableStateOf("mateo.rivas@gmail.com") }
            var pass by remember { mutableStateOf("secreto123") }
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                YupanaTextField(email, { email = it }, label = "Correo electrónico", modifier = Modifier.fillMaxWidth())
                YupanaTextField(pass, { pass = it }, label = "Contraseña", isPassword = true, modifier = Modifier.fillMaxWidth())
            }
        }

        Seccion("Tarjeta") {
            YupanaCard(Modifier.fillMaxWidth()) {
                Text("Tarjeta base", style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
                Spacer(Modifier.height(spacing.xs))
                Text("Superficie con borde y esquinas de 18dp.", style = YupanaTheme.typography.body, color = colors.textoSecundario)
            }
        }

        Seccion("Chips de estado") {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                YupanaChip("Vence hoy", YupanaEstado.Hoy)
                YupanaChip("En 3 días", YupanaEstado.Pronto)
                YupanaChip("Al día", YupanaEstado.AlDia)
            }
        }

        Seccion("Logos y avatar") {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm), verticalAlignment = Alignment.CenterVertically) {
                YupanaServiceLogo("N", Color(0xFFE50914))
                YupanaServiceLogo("S", Color(0xFF1DB954))
                YupanaServiceLogo("D+", Color(0xFF0A47A1))
                YupanaAvatar("M")
            }
        }

        Seccion("TopBar") {
            YupanaCard(Modifier.fillMaxWidth(), padding = spacing.xs) {
                YupanaTopBar("Mis cuentas", onBack = {}, action = {
                    Text("⚙", style = YupanaTheme.typography.subtitle, color = colors.textoSecundario)
                })
            }
        }

        Seccion("Métricas") {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                YupanaStatCard("3", "Vencen hoy", valueColor = colors.peligro, modifier = Modifier.weight(1f))
                YupanaStatCard("8", "Próximos 7 días", modifier = Modifier.weight(1f))
                YupanaStatCard("S/.412", "Por cobrar", modifier = Modifier.weight(1f))
            }
        }

        Seccion("Item de vencimiento") {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                YupanaAssignmentCard("Rosa Quispe", "Netflix · Perfil 2", "18", YupanaEstado.Hoy, "Vence hoy", "N", Color(0xFFE50914))
                YupanaAssignmentCard("Carlos Mamani", "Spotify · Familiar", "12", YupanaEstado.Pronto, "En 3 días", "S", Color(0xFF1DB954))
                YupanaAssignmentCard("José Vargas", "HBO Max · Perfil 3", "20", YupanaEstado.AlDia, "En 12 días", "H", Color(0xFF7B2FF7))
            }
        }

        Seccion("Navegación inferior") {
            var sel by remember { mutableStateOf(0) }
            YupanaBottomNav(selectedIndex = sel, onSelect = { sel = it }, modifier = Modifier.clip(YupanaTheme.shapes.medium))
        }

        Seccion("Estado vacío") {
            YupanaEmptyState("Sin cuentas aún", "Agrega tu primera cuenta para empezar a gestionar.", actionText = "Agregar cuenta", onAction = {})
        }
        Seccion("Estado de carga") { YupanaLoadingState() }
        Seccion("Estado de error") {
            YupanaErrorState("No pudimos cargar tus cuentas. Revisa tu conexión.", onRetry = {})
        }

        Spacer(Modifier.height(spacing.xxl))
    }
}

@Composable
private fun Seccion(titulo: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(YupanaTheme.spacing.sm)) {
        Text(titulo, style = YupanaTheme.typography.label, color = YupanaTheme.colors.acento)
        content()
    }
}

@Composable
private fun ColoresShowcase() {
    val c = YupanaTheme.colors
    val swatches = listOf(
        "fondo" to c.fondo,
        "superficie" to c.superficie,
        "superficieElevada" to c.superficieElevada,
        "marca" to c.marca,
        "acento" to c.acento,
        "textoPrincipal" to c.textoPrincipal,
        "textoSecundario" to c.textoSecundario,
        "lineas" to c.lineas,
        "exito" to c.exito,
        "alerta" to c.alerta,
        "peligro" to c.peligro,
    )
    Column(verticalArrangement = Arrangement.spacedBy(YupanaTheme.spacing.xs)) {
        swatches.forEach { (name, color) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(YupanaTheme.spacing.sm)) {
                Box(Modifier.size(28.dp).clip(YupanaTheme.shapes.small).background(color))
                Text(name, style = YupanaTheme.typography.body, color = c.textoSecundario)
            }
        }
    }
}

@Composable
private fun TipografiaShowcase() {
    val t = YupanaTheme.typography
    val c = YupanaTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(YupanaTheme.spacing.xs)) {
        Text("Display", style = t.display, color = c.textoPrincipal)
        Text("Title", style = t.title, color = c.textoPrincipal)
        Text("Subtitle", style = t.subtitle, color = c.textoPrincipal)
        Text("Body — gestiona tus cuentas", style = t.body, color = c.textoPrincipal)
        Text("Label", style = t.label, color = c.textoSecundario)
        Text("Caption", style = t.caption, color = c.textoSecundario)
    }
}

@Composable
private fun EspaciadoShowcase() {
    val s = YupanaTheme.spacing
    val c = YupanaTheme.colors
    val escala = listOf("xs" to s.xs, "sm" to s.sm, "md" to s.md, "lg" to s.lg, "xl" to s.xl, "xxl" to s.xxl)
    Column(verticalArrangement = Arrangement.spacedBy(s.xs)) {
        escala.forEach { (name, dp) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(s.sm)) {
                Box(Modifier.width(dp).height(12.dp).background(c.marca))
                Text(name, style = YupanaTheme.typography.caption, color = c.textoSecundario)
            }
        }
    }
}
