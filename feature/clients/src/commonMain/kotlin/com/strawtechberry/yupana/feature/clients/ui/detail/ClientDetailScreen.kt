package com.strawtechberry.yupana.feature.clients.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.clients.domain.model.Client
import com.strawtechberry.yupana.feature.clients.domain.model.ClientAssignment
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Client detail screen (stateless): client summary + its active/cancelled subscriptions. */
@Composable
fun ClientDetailScreen(
    state: ClientDetailUiState,
    onIntent: (ClientDetailIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = state.client?.name ?: "Cliente",
            onBack = { onIntent(ClientDetailIntent.BackClicked) },
            action = {
                IconButton(onClick = { onIntent(ClientDetailIntent.EditClicked) }) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Editar cliente", tint = colors.marca)
                }
            },
        )

        when {
            state.isLoading && state.client == null -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(ClientDetailIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.client != null -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                item { ClientSummaryCard(state.client) }
                item {
                    Text(
                        text = "Suscripciones activas",
                        style = YupanaTheme.typography.label,
                        color = colors.textoSecundario,
                        modifier = Modifier.padding(top = spacing.sm),
                    )
                }
                if (state.activeAssignments.isEmpty()) {
                    item {
                        YupanaEmptyState(
                            title = "Sin suscripciones activas",
                            message = "Este cliente no tiene perfiles asignados por ahora.",
                        )
                    }
                } else {
                    items(state.activeAssignments, key = { it.assignmentId }) { assignment ->
                        ClientAssignmentItem(assignment)
                    }
                }
                if (state.cancelledAssignments.isNotEmpty()) {
                    item {
                        Text(
                            text = "Historial",
                            style = YupanaTheme.typography.label,
                            color = colors.textoSecundario,
                            modifier = Modifier.padding(top = spacing.sm),
                        )
                    }
                    items(state.cancelledAssignments, key = { it.assignmentId }) { assignment ->
                        ClientAssignmentItem(assignment)
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientSummaryCard(client: Client) {
    val colors = YupanaTheme.colors
    YupanaCard(modifier = Modifier.fillMaxWidth()) {
        client.contact?.takeIf { it.isNotBlank() }?.let {
            Text(it, style = YupanaTheme.typography.body, color = colors.textoPrincipal)
        }
        client.notes?.takeIf { it.isNotBlank() }?.let {
            Text(it, style = YupanaTheme.typography.caption, color = colors.textoSecundario)
        }
    }
}

@Composable
private fun ClientAssignmentItem(assignment: ClientAssignment) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val isCancelled = assignment.status != "active"

    YupanaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    "${assignment.serviceName} · ${assignment.accountAlias} · ${assignment.profileLabel}",
                    style = YupanaTheme.typography.subtitle,
                    color = if (isCancelled) colors.textoSecundario else colors.textoPrincipal,
                )
                assignment.dueDate?.let {
                    Text("Vence: $it", style = YupanaTheme.typography.caption, color = colors.textoSecundario)
                }
            }
            assignment.priceCharged?.let {
                Text(
                    "S/. $it",
                    style = YupanaTheme.typography.price,
                    color = if (isCancelled) colors.textoSecundario else colors.acento,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClientDetailScreenPreview() {
    ClientDetailScreen(
        state = ClientDetailUiState(
            clientId = "c1",
            client = Client(id = "c1", name = "María López", contact = "+51 999 999 999", notes = "Amiga", createdAt = null),
            activeAssignments = listOf(
                ClientAssignment(
                    assignmentId = "a1", status = "active", priceCharged = 20.0, dueDate = "2026-08-01",
                    profileLabel = "Perfil 1", accountAlias = "Cuenta principal", serviceName = "Netflix",
                ),
            ),
        ),
        onIntent = {},
    )
}
