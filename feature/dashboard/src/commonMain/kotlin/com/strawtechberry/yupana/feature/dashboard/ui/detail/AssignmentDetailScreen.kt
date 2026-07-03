package com.strawtechberry.yupana.feature.dashboard.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Assignment detail screen (stateless): resumen + acciones (renovar/editar/liberar/mover). */
@Composable
fun AssignmentDetailScreen(
    state: AssignmentDetailUiState,
    onIntent: (AssignmentDetailIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val expiration = state.expiration

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(title = "Detalle de asignación", onBack = { onIntent(AssignmentDetailIntent.BackClicked) })

        when {
            state.isLoading && expiration == null -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null && expiration == null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(AssignmentDetailIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            expiration != null -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(spacing.lg),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Text(expiration.clientName, style = YupanaTheme.typography.title, color = colors.textoPrincipal)
                    Spacer(Modifier.height(spacing.sm))
                    Text(
                        text = "${expiration.serviceName} · ${expiration.accountAlias}",
                        style = YupanaTheme.typography.body,
                        color = colors.textoSecundario,
                    )
                    Text(
                        text = "Perfil: ${expiration.profileLabel}",
                        style = YupanaTheme.typography.body,
                        color = colors.textoSecundario,
                    )
                    Spacer(Modifier.height(spacing.sm))
                    Text(
                        text = "Precio: S/.${expiration.priceCharged ?: 0.0}",
                        style = YupanaTheme.typography.price,
                        color = colors.acento,
                    )
                    Text(
                        text = "Vence: ${expiration.dueDate ?: "-"}",
                        style = YupanaTheme.typography.body,
                        color = colors.textoPrincipal,
                    )
                    Text("Estado: Activa", style = YupanaTheme.typography.body, color = colors.exito)
                }

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = colors.peligro,
                        style = YupanaTheme.typography.body,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                YupanaButton(
                    text = "Marcar como cobrado / renovar",
                    onClick = { onIntent(AssignmentDetailIntent.RenewClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    loading = state.isRenewing,
                )
                YupanaButton(
                    text = "Editar precio o vencimiento",
                    onClick = { onIntent(AssignmentDetailIntent.EditClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    variant = YupanaButtonVariant.Secondary,
                )
                YupanaButton(
                    text = "Liberar perfil",
                    onClick = { onIntent(AssignmentDetailIntent.LiberateClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    variant = YupanaButtonVariant.Destructive,
                )
                YupanaButton(
                    text = "Mover a otra cuenta",
                    onClick = { onIntent(AssignmentDetailIntent.MoveClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    variant = YupanaButtonVariant.Text,
                )
            }
        }
    }

    if (state.showEditDialog) {
        EditAssignmentDialog(state = state, onIntent = onIntent)
    }

    if (state.showLiberateConfirm) {
        AlertDialog(
            onDismissRequest = { onIntent(AssignmentDetailIntent.DismissLiberateConfirm) },
            title = { Text("Liberar perfil", color = colors.textoPrincipal) },
            text = {
                Text(
                    text = "El cliente dejará de estar asignado a este perfil. ¿Continuar?",
                    color = colors.textoSecundario,
                )
            },
            confirmButton = {
                YupanaButton(
                    text = "Liberar",
                    variant = YupanaButtonVariant.Destructive,
                    onClick = { onIntent(AssignmentDetailIntent.ConfirmLiberate) },
                    loading = state.isLiberating,
                )
            },
            dismissButton = {
                YupanaButton(
                    text = "Cancelar",
                    variant = YupanaButtonVariant.Text,
                    onClick = { onIntent(AssignmentDetailIntent.DismissLiberateConfirm) },
                )
            },
        )
    }
}

@Preview
@Composable
private fun AssignmentDetailScreenPreview() {
    AssignmentDetailScreen(
        state = AssignmentDetailUiState(
            assignmentId = "1",
            expiration = UpcomingExpiration(
                assignmentId = "1", dueDate = "2026-07-01", daysLeft = 0, priceCharged = 20.0,
                clientName = "María López", clientContact = null, profileLabel = "Perfil 2",
                accountAlias = "Cuenta principal", serviceName = "Netflix",
            ),
        ),
        onIntent = {},
    )
}
