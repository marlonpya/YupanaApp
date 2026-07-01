package com.strawtechberry.yupana.feature.auth.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaBrandLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaKhipuMotif
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.auth.ui.common.AuthScreenContainer

/** Login screen (stateless). Receives the state and emits intents via [onIntent]. */
@Composable
fun LoginScreen(
    state: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
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
            text = "Bienvenido de vuelta.\nGestiona tus cuentas en un solo lugar.",
            style = YupanaTheme.typography.body,
            color = colors.textoSecundario,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(spacing.xxl))

        YupanaTextField(
            value = state.email,
            onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
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
            onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
            label = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Rounded.Lock,
            isPassword = true,
            errorText = state.passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        )

        Text(
            text = "¿Olvidaste tu contraseña?",
            style = YupanaTheme.typography.label,
            color = colors.textoSecundario,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = spacing.sm)
                .clickable { onIntent(LoginIntent.ForgotPasswordClicked) },
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

        Spacer(Modifier.height(spacing.xl))
        YupanaButton(
            text = "Iniciar sesión",
            onClick = { onIntent(LoginIntent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            loading = state.isLoading,
        )

        Spacer(Modifier.height(spacing.xxl))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text("¿No tienes cuenta? ", style = YupanaTheme.typography.body, color = colors.textoSecundario)
            Text(
                text = "Crear cuenta",
                style = YupanaTheme.typography.body.copy(fontWeight = FontWeight.SemiBold),
                color = colors.acento,
                modifier = Modifier.clickable { onIntent(LoginIntent.NavigateToRegister) },
            )
        }
        Spacer(Modifier.height(spacing.lg))
    }
}
