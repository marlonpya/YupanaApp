package com.strawtechberry.yupana.feature.assignment.ui.assign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Client selector: pick an existing client (with search) or create a new one inline. */
@Composable
fun ClientPickerSection(
    state: AssignUiState,
    onIntent: (AssignIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Cliente", style = YupanaTheme.typography.label, color = colors.textoSecundario)
        Spacer(Modifier.height(spacing.sm))

        YupanaTextField(
            value = state.clientQuery,
            onValueChange = { onIntent(AssignIntent.ClientQueryChanged(it)) },
            label = "Buscar cliente",
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(spacing.sm))

        Box {
            YupanaCard(modifier = Modifier.fillMaxWidth().clickable { expanded = true }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = state.selectedClientName ?: "Selecciona un cliente",
                        style = YupanaTheme.typography.body,
                        color = if (state.selectedClientName != null) colors.textoPrincipal else colors.textoSecundario,
                    )
                    Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = colors.textoSecundario)
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (state.filteredClients.isEmpty()) {
                    DropdownMenuItem(text = { Text("Sin resultados") }, onClick = {}, enabled = false)
                }
                state.filteredClients.forEach { client ->
                    DropdownMenuItem(
                        text = { Text(client.name) },
                        onClick = {
                            onIntent(AssignIntent.ClientSelected(client.id))
                            expanded = false
                        },
                    )
                }
            }
        }

        Spacer(Modifier.height(spacing.sm))
        YupanaButton(
            text = "+ Crear nuevo cliente",
            variant = YupanaButtonVariant.Primary,
            onClick = { onIntent(AssignIntent.CreateClientClicked) },
        )
    }

    if (state.showCreateClientDialog) {
        CreateClientDialog(state = state, onIntent = onIntent)
    }
}

@Composable
private fun CreateClientDialog(
    state: AssignUiState,
    onIntent: (AssignIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    AlertDialog(
        onDismissRequest = { onIntent(AssignIntent.DismissCreateClientDialog) },
        title = { Text("Nuevo cliente", color = colors.textoPrincipal) },
        text = {
            Column {
                YupanaTextField(
                    value = state.newClientName,
                    onValueChange = { onIntent(AssignIntent.NewClientNameChanged(it)) },
                    label = "Nombre",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(spacing.md))
                YupanaTextField(
                    value = state.newClientContact,
                    onValueChange = { onIntent(AssignIntent.NewClientContactChanged(it)) },
                    label = "Contacto / WhatsApp (opcional)",
                    modifier = Modifier.fillMaxWidth(),
                )
                if (state.createClientError != null) {
                    Spacer(Modifier.height(spacing.md))
                    Text(
                        text = state.createClientError,
                        color = colors.peligro,
                        style = YupanaTheme.typography.body,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        confirmButton = {
            YupanaButton(
                text = "Crear",
                onClick = { onIntent(AssignIntent.ConfirmCreateClient) },
                enabled = state.newClientName.isNotBlank() && !state.isCreatingClient,
                loading = state.isCreatingClient,
            )
        },
        dismissButton = {
            YupanaButton(
                text = "Cancelar",
                variant = YupanaButtonVariant.Text,
                onClick = { onIntent(AssignIntent.DismissCreateClientDialog) },
            )
        },
    )
}
