package com.strawtechberry.yupana.feature.accounts.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaServiceLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.ui.common.parseServiceColor
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Account form screen (stateless): create or edit, depending on [AccountFormUiState.isEditMode]. */
@Composable
fun AccountFormScreen(
    state: AccountFormUiState,
    onIntent: (AccountFormIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = if (state.isEditMode) "Editar cuenta" else "Nueva cuenta",
            onBack = { onIntent(AccountFormIntent.BackClicked) },
        )

        if (state.isLoadingAccount) {
            YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            return@Column
        }

        Column(modifier = Modifier.padding(spacing.lg)) {
            ServiceSelector(state = state, onClick = { onIntent(AccountFormIntent.PickServiceClicked) })

            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.alias,
                onValueChange = { onIntent(AccountFormIntent.AliasChanged(it)) },
                label = "Alias",
                modifier = Modifier.fillMaxWidth(),
                errorText = state.aliasError,
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.monthlyCost,
                onValueChange = { onIntent(AccountFormIntent.MonthlyCostChanged(it)) },
                label = "Costo mensual (S/.)",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Decimal,
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.numProfiles,
                onValueChange = { onIntent(AccountFormIntent.NumProfilesChanged(it)) },
                label = "Número de perfiles",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.billingDay,
                onValueChange = { onIntent(AccountFormIntent.BillingDayChanged(it)) },
                label = "Día de corte (1-31)",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.notes,
                onValueChange = { onIntent(AccountFormIntent.NotesChanged(it)) },
                label = "Notas",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
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
                text = "Guardar",
                onClick = { onIntent(AccountFormIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isSaveEnabled,
                loading = state.isLoading,
            )
        }
    }
}

@Composable
private fun ServiceSelector(state: AccountFormUiState, onClick: () -> Unit) {
    val colors = YupanaTheme.colors
    Column {
        Text("Servicio", style = YupanaTheme.typography.label, color = colors.textoSecundario)
        Spacer(Modifier.height(YupanaTheme.spacing.sm))
        YupanaCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.serviceId != null) {
                    YupanaServiceLogo(
                        text = state.serviceName?.take(1)?.uppercase() ?: "?",
                        color = parseServiceColor(state.serviceColor) ?: colors.marca,
                    )
                    Spacer(Modifier.width(YupanaTheme.spacing.md))
                    Text(
                        text = state.serviceName ?: "Servicio",
                        style = YupanaTheme.typography.subtitle,
                        color = colors.textoPrincipal,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Text(
                        text = "Selecciona un servicio",
                        style = YupanaTheme.typography.body,
                        color = colors.textoSecundario,
                        modifier = Modifier.weight(1f),
                    )
                }
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = colors.textoSecundario)
            }
        }
        if (state.serviceError != null) {
            Spacer(Modifier.height(YupanaTheme.spacing.xs))
            Text(state.serviceError, style = YupanaTheme.typography.caption, color = colors.peligro)
        }
    }
}

@Preview
@Composable
private fun AccountFormScreenPreview() {
    AccountFormScreen(
        state = AccountFormUiState(serviceId = "1", serviceName = "Netflix", serviceColor = "#E50914", alias = "Cuenta principal"),
        onIntent = {},
    )
}
