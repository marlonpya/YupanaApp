package com.strawtechberry.yupana.feature.clients.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Client form screen (stateless): create or edit, depending on [ClientFormUiState.isEditMode]. */
@Composable
fun ClientFormScreen(
    state: ClientFormUiState,
    onIntent: (ClientFormIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = if (state.isEditMode) "Editar cliente" else "Nuevo cliente",
            onBack = { onIntent(ClientFormIntent.BackClicked) },
        )

        if (state.isLoadingClient) {
            YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            return@Column
        }

        Column(modifier = Modifier.padding(spacing.lg)) {
            YupanaTextField(
                value = state.name,
                onValueChange = { onIntent(ClientFormIntent.NameChanged(it)) },
                label = "Nombre",
                modifier = Modifier.fillMaxWidth(),
                errorText = state.nameError,
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.contact,
                onValueChange = { onIntent(ClientFormIntent.ContactChanged(it)) },
                label = "Contacto / WhatsApp",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(spacing.md))
            YupanaTextField(
                value = state.notes,
                onValueChange = { onIntent(ClientFormIntent.NotesChanged(it)) },
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
                onClick = { onIntent(ClientFormIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isSaveEnabled,
                loading = state.isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun ClientFormScreenPreview() {
    ClientFormScreen(state = ClientFormUiState(name = "María López"), onIntent = {})
}
