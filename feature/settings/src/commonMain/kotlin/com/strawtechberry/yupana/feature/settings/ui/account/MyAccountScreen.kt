package com.strawtechberry.yupana.feature.settings.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyAccountScreen(
    state: MyAccountUiState,
    onIntent: (MyAccountIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(title = "Mi cuenta", onBack = { onIntent(MyAccountIntent.BackClicked) })

        if (state.isLoading) {
            YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(spacing.lg),
                verticalArrangement = Arrangement.spacedBy(spacing.lg),
            ) {
                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Correo", style = YupanaTheme.typography.label, color = colors.textoSecundario)
                    Spacer(Modifier.height(spacing.xs))
                    Text(state.email ?: "-", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
                }

                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Cambiar contraseña", style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
                    Spacer(Modifier.height(spacing.md))

                    YupanaTextField(
                        value = state.currentPassword,
                        onValueChange = { onIntent(MyAccountIntent.CurrentPasswordChanged(it)) },
                        label = "Contraseña actual",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(spacing.md))
                    YupanaTextField(
                        value = state.newPassword,
                        onValueChange = { onIntent(MyAccountIntent.NewPasswordChanged(it)) },
                        label = "Nueva contraseña",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(spacing.md))
                    YupanaTextField(
                        value = state.confirmPassword,
                        onValueChange = { onIntent(MyAccountIntent.ConfirmPasswordChanged(it)) },
                        label = "Confirmar nueva contraseña",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(spacing.md))

                    if (state.error != null) {
                        Text(state.error, style = YupanaTheme.typography.body, color = colors.peligro)
                        Spacer(Modifier.height(spacing.sm))
                    }
                    if (state.passwordChanged) {
                        Text("Contraseña actualizada", style = YupanaTheme.typography.body, color = colors.exito)
                        Spacer(Modifier.height(spacing.sm))
                    }

                    YupanaButton(
                        text = "Cambiar contraseña",
                        onClick = { onIntent(MyAccountIntent.ChangePasswordClicked) },
                        enabled = state.canSubmit,
                        loading = state.isChangingPassword,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyAccountScreenPreview() {
    MyAccountScreen(
        state = MyAccountUiState(isLoading = false, email = "marlon@correo.com"),
        onIntent = {},
    )
}
