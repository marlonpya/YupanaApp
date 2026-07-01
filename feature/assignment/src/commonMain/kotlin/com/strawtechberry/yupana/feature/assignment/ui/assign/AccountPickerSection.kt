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
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme

/** Account selector: locked (read-only) when arriving from a specific account's detail. */
@Composable
fun AccountPickerSection(
    state: AssignUiState,
    onIntent: (AssignIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Servicio / cuenta", style = YupanaTheme.typography.label, color = colors.textoSecundario)
        Spacer(Modifier.height(spacing.sm))
        Box {
            YupanaCard(
                modifier = Modifier.fillMaxWidth().let { base ->
                    if (state.accountLocked) base else base.clickable { expanded = true }
                },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = state.accountLabel ?: "Selecciona una cuenta",
                        style = YupanaTheme.typography.body,
                        color = if (state.accountLabel != null) colors.textoPrincipal else colors.textoSecundario,
                    )
                    if (!state.accountLocked) {
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = colors.textoSecundario)
                    }
                }
            }
            if (!state.accountLocked) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    state.accountGroups.forEach { group ->
                        group.accounts.forEach { accountWithOccupancy ->
                            val label = "${group.service.name} · ${accountWithOccupancy.account.alias}"
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onIntent(AssignIntent.AccountSelected(accountWithOccupancy.account.id, label))
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
