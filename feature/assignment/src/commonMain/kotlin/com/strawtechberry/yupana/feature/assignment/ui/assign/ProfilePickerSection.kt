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

/** Free-profile selector for the chosen account; locked when arriving from a specific profile. */
@Composable
fun ProfilePickerSection(
    state: AssignUiState,
    onIntent: (AssignIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    var expanded by remember { mutableStateOf(false) }
    val interactive = !state.profileLocked && state.accountId != null

    Column {
        Text("Perfil disponible", style = YupanaTheme.typography.label, color = colors.textoSecundario)
        Spacer(Modifier.height(spacing.sm))
        Box {
            YupanaCard(
                modifier = Modifier.fillMaxWidth().let { base -> if (interactive) base.clickable { expanded = true } else base },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val placeholder = if (state.accountId == null) "Elige primero una cuenta" else "Selecciona un perfil"
                    Text(
                        text = state.profileLabel ?: placeholder,
                        style = YupanaTheme.typography.body,
                        color = if (state.profileLabel != null) colors.textoPrincipal else colors.textoSecundario,
                    )
                    if (interactive) {
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = colors.textoSecundario)
                    }
                }
            }
            if (interactive) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (state.availableProfiles.isEmpty()) {
                        DropdownMenuItem(text = { Text("Sin perfiles libres") }, onClick = {}, enabled = false)
                    }
                    state.availableProfiles.forEach { profile ->
                        DropdownMenuItem(
                            text = { Text(profile.label) },
                            onClick = {
                                onIntent(AssignIntent.ProfileSelected(profile.id, profile.label))
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
