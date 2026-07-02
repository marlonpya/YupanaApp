package com.strawtechberry.yupana.feature.dashboard.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
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
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.assignment.ui.common.epochMillisToIsoDate

/** "Editar precio o vencimiento": precio + fecha (reusa el date picker nativo del Grupo 4). */
@Composable
fun EditAssignmentDialog(
    state: AssignmentDetailUiState,
    onIntent: (AssignmentDetailIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onIntent(AssignmentDetailIntent.DismissEditDialog) },
        title = { Text("Editar precio o vencimiento", color = colors.textoPrincipal) },
        text = {
            Column {
                YupanaTextField(
                    value = state.editPrice,
                    onValueChange = { onIntent(AssignmentDetailIntent.EditPriceChanged(it)) },
                    label = "Precio a cobrar (S/.)",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Decimal,
                )
                Spacer(Modifier.height(spacing.md))
                Text("Fecha de vencimiento", style = YupanaTheme.typography.label, color = colors.textoSecundario)
                Spacer(Modifier.height(spacing.sm))
                YupanaCard(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                    Text(
                        text = state.editDueDate.ifBlank { "Selecciona una fecha" },
                        style = YupanaTheme.typography.body,
                        color = if (state.editDueDate.isNotBlank()) colors.textoPrincipal else colors.textoSecundario,
                    )
                }
                if (state.editError != null) {
                    Spacer(Modifier.height(spacing.md))
                    Text(
                        text = state.editError,
                        color = colors.peligro,
                        style = YupanaTheme.typography.body,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        confirmButton = {
            YupanaButton(
                text = "Guardar",
                onClick = { onIntent(AssignmentDetailIntent.ConfirmEdit) },
                enabled = !state.isSavingEdit,
                loading = state.isSavingEdit,
            )
        },
        dismissButton = {
            YupanaButton(
                text = "Cancelar",
                variant = YupanaButtonVariant.Text,
                onClick = { onIntent(AssignmentDetailIntent.DismissEditDialog) },
            )
        },
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                YupanaButton(
                    text = "Aceptar",
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onIntent(AssignmentDetailIntent.EditDueDateChanged(epochMillisToIsoDate(millis)))
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
