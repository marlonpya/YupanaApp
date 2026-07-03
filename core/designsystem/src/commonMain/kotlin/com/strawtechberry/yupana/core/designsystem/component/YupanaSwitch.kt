package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Switch on/off temado con la paleta de Yupana (marca = encendido). */
@Composable
fun YupanaSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.onMarca,
            checkedTrackColor = colors.marca,
            checkedBorderColor = colors.marca,
            uncheckedThumbColor = colors.textoSecundario,
            uncheckedTrackColor = colors.superficieElevada,
            uncheckedBorderColor = colors.lineas,
        ),
    )
}

@Preview
@Composable
private fun YupanaSwitchPreview() {
    androidx.compose.foundation.layout.Row {
        YupanaSwitch(checked = true, onCheckedChange = {})
        YupanaSwitch(checked = false, onCheckedChange = {})
    }
}
