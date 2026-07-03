package com.strawtechberry.yupana.feature.assignment.ui.assign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.assignment.ui.common.epochMillisToIsoDate
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Assign screen (stateless): a single scrollable form, fields in the order described. */
@Composable
fun AssignScreen(
    state: AssignUiState,
    onIntent: (AssignIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(title = "Asignar perfil", onBack = { onIntent(AssignIntent.BackClicked) })

        if (state.isLoadingOptions && state.accountGroups.isEmpty() && state.availableProfiles.isEmpty()) {
            YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            return@Column
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacing.lg),
        ) {
            AccountPickerSection(state = state, onIntent = onIntent)
            Spacer(Modifier.height(spacing.lg))

            ProfilePickerSection(state = state, onIntent = onIntent)
            Spacer(Modifier.height(spacing.lg))

            ClientPickerSection(state = state, onIntent = onIntent)
            Spacer(Modifier.height(spacing.lg))

            YupanaTextField(
                value = state.priceCharged,
                onValueChange = { onIntent(AssignIntent.PriceChanged(it)) },
                label = "Precio a cobrar (S/.)",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Decimal,
            )
            Spacer(Modifier.height(spacing.md))

            Column {
                Text("Fecha de vencimiento", style = YupanaTheme.typography.label, color = colors.textoSecundario)
                Spacer(Modifier.height(spacing.sm))
                YupanaCard(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                    Text(
                        text = state.dueDate.ifBlank { "Selecciona una fecha" },
                        style = YupanaTheme.typography.body,
                        color = if (state.dueDate.isNotBlank()) colors.textoPrincipal else colors.textoSecundario,
                    )
                }
            }

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
                text = "Asignar perfil",
                onClick = { onIntent(AssignIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isSaveEnabled,
                loading = state.isSaving,
            )
            Spacer(Modifier.height(spacing.lg))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                YupanaButton(
                    text = "Aceptar",
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onIntent(AssignIntent.DueDateChanged(epochMillisToIsoDate(millis)))
                        }
                        showDatePicker = false
                    },
                )
            },
            dismissButton = {
                YupanaButton(text = "Cancelar", variant = YupanaButtonVariant.Text, onClick = { showDatePicker = false })
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
private fun AssignScreenPreview() {
    AssignScreen(
        state = AssignUiState(
            accountLabel = "Netflix · Cuenta principal",
            accountLocked = true,
            profileLabel = "Perfil 2",
            profileLocked = true,
            selectedClientName = "María López",
            priceCharged = "20",
            dueDate = "2026-08-01",
        ),
        onIntent = {},
    )
}
