package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Campo de texto outlined oscuro: borde `lineas`, foco `marca`, label flotante. */
@Composable
fun YupanaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
) {
    val colors = YupanaTheme.colors
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine,
        shape = YupanaTheme.shapes.small,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.marca,
            unfocusedBorderColor = colors.lineas,
            focusedTextColor = colors.textoPrincipal,
            unfocusedTextColor = colors.textoPrincipal,
            cursorColor = colors.marca,
            focusedLabelColor = colors.marca,
            unfocusedLabelColor = colors.textoSecundario,
            focusedContainerColor = colors.fondo,
            unfocusedContainerColor = colors.fondo,
            focusedPlaceholderColor = colors.textoSecundario,
            unfocusedPlaceholderColor = colors.textoSecundario,
        ),
    )
}
