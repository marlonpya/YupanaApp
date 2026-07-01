package com.strawtechberry.yupana.core.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/**
 * Campo de texto outlined oscuro (esquina 14dp, calca el diseño). Soporta ícono a la izquierda,
 * error inline ([errorText]) y, si [isPassword], un ojo interno para mostrar/ocultar el valor.
 */
@Composable
fun YupanaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    singleLine: Boolean = true,
) {
    val colors = YupanaTheme.colors
    val esError = errorText != null
    var passwordVisible by remember { mutableStateOf(false) }
    val ocultar = isPassword && !passwordVisible

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine,
        isError = esError,
        supportingText = errorText?.let { { Text(it) } },
        shape = RoundedCornerShape(14.dp),
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    )
                }
            }
        } else null,
        visualTransformation = if (ocultar) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.marca,
            unfocusedBorderColor = colors.lineas,
            errorBorderColor = colors.peligro,
            focusedTextColor = colors.textoPrincipal,
            unfocusedTextColor = colors.textoPrincipal,
            cursorColor = colors.marca,
            errorCursorColor = colors.peligro,
            focusedLabelColor = colors.marca,
            unfocusedLabelColor = colors.textoSecundario,
            errorLabelColor = colors.peligro,
            focusedLeadingIconColor = colors.marca,
            unfocusedLeadingIconColor = colors.textoSecundario,
            focusedTrailingIconColor = colors.textoSecundario,
            unfocusedTrailingIconColor = colors.textoSecundario,
            focusedContainerColor = colors.fondo,
            unfocusedContainerColor = colors.fondo,
            errorContainerColor = colors.fondo,
            focusedPlaceholderColor = colors.textoSecundario,
            unfocusedPlaceholderColor = colors.textoSecundario,
            errorSupportingTextColor = colors.peligro,
        ),
    )
}
