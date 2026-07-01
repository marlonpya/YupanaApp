package com.strawtechberry.yupana.feature.auth.ui.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaBrandLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaKhipuMotif
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.auth.ui.common.AuthScreenContainer

/** Pantalla de Registro (stateless). */
@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    AuthScreenContainer {
        Spacer(Modifier.height(spacing.md))
        YupanaKhipuMotif()
        Spacer(Modifier.height(spacing.xl))
        YupanaBrandLogo()
        Spacer(Modifier.height(spacing.lg))
        Text(
            text = "Crea tu cuenta.\nOrganiza tus suscripciones en un solo lugar.",
            style = YupanaTheme.typography.body,
            color = colors.textoSecundario,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(spacing.xxl))

        YupanaTextField(
            value = state.email,
            onValueChange = { onIntent(RegisterIntent.EmailCambiado(it)) },
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Rounded.Mail,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(spacing.md))
        YupanaTextField(
            value = state.password,
            onValueChange = { onIntent(RegisterIntent.PasswordCambiado(it)) },
            label = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Rounded.Lock,
            isPassword = true,
            errorText = state.passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(spacing.md))
        YupanaTextField(
            value = state.confirmPassword,
            onValueChange = { onIntent(RegisterIntent.ConfirmCambiado(it)) },
            label = "Confirmar contraseña",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Rounded.Lock,
            isPassword = true,
            errorText = state.confirmError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        )

        if (state.formError != null) {
            Spacer(Modifier.height(spacing.md))
            Text(
                text = state.formError,
                style = YupanaTheme.typography.body,
                color = colors.peligro,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (state.avisoConfirmacion != null) {
            Spacer(Modifier.height(spacing.md))
            Text(
                text = state.avisoConfirmacion,
                style = YupanaTheme.typography.body,
                color = colors.exito,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(spacing.xl))
        YupanaButton(
            text = "Crear cuenta",
            onClick = { onIntent(RegisterIntent.Enviar) },
            modifier = Modifier.fillMaxWidth(),
            loading = state.cargando,
        )

        Spacer(Modifier.height(spacing.xxl))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text("¿Ya tienes cuenta? ", style = YupanaTheme.typography.body, color = colors.textoSecundario)
            Text(
                text = "Inicia sesión",
                style = YupanaTheme.typography.body.copy(fontWeight = FontWeight.SemiBold),
                color = colors.acento,
                modifier = Modifier.clickable { onIntent(RegisterIntent.IrALogin) },
            )
        }
        Spacer(Modifier.height(spacing.lg))
    }
}
