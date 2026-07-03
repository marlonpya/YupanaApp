package com.strawtechberry.yupana.feature.auth.ui.resetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.auth.generated.resources.Res
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_back_to_login
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_email_label
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_submit
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_subtitle
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_success_message
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_success_title
import com.strawtechberry.yupana.feature.auth.generated.resources.reset_password_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Reset password screen (stateless): asks for the email, then shows a confirmation state. */
@Composable
fun ResetPasswordScreen(
    state: ResetPasswordUiState,
    onIntent: (ResetPasswordIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(
            title = stringResource(Res.string.reset_password_title),
            onBack = { onIntent(ResetPasswordIntent.BackClicked) },
        )

        if (state.isSent) {
            Column(modifier = Modifier.padding(spacing.lg)) {
                Text(
                    text = stringResource(Res.string.reset_password_success_title),
                    style = YupanaTheme.typography.title,
                    color = colors.exito,
                )
                Spacer(Modifier.height(spacing.sm))
                Text(
                    text = stringResource(Res.string.reset_password_success_message),
                    style = YupanaTheme.typography.body,
                    color = colors.textoSecundario,
                )
                Spacer(Modifier.height(spacing.xl))
                YupanaButton(
                    text = stringResource(Res.string.reset_password_back_to_login),
                    onClick = { onIntent(ResetPasswordIntent.BackClicked) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            return@Column
        }

        Column(modifier = Modifier.padding(spacing.lg)) {
            Text(
                text = stringResource(Res.string.reset_password_subtitle),
                style = YupanaTheme.typography.body,
                color = colors.textoSecundario,
            )
            Spacer(Modifier.height(spacing.xl))
            YupanaTextField(
                value = state.email,
                onValueChange = { onIntent(ResetPasswordIntent.EmailChanged(it)) },
                label = stringResource(Res.string.reset_password_email_label),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Rounded.Mail,
                errorText = state.emailError,
                keyboardType = KeyboardType.Email,
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

            Spacer(Modifier.height(spacing.xl))
            YupanaButton(
                text = stringResource(Res.string.reset_password_submit),
                onClick = { onIntent(ResetPasswordIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                loading = state.isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(state = ResetPasswordUiState(email = "admin@yupana.pe"), onIntent = {})
}
