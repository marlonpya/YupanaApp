package com.strawtechberry.yupana.feature.dashboard.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaAssignmentCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaAvatar
import com.strawtechberry.yupana.core.designsystem.component.YupanaEstado
import com.strawtechberry.yupana.core.designsystem.component.YupanaStatCard
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Dashboard "Próximos a vencer" (stateless): saludo, métricas y lista por urgencia. */
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onIntent: (DashboardIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(spacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Hola 👋", style = YupanaTheme.typography.title, color = colors.textoPrincipal)
                Text("Esto es lo que vence pronto", style = YupanaTheme.typography.body, color = colors.textoSecundario)
            }
            YupanaAvatar(initial = "Y")
        }

        when {
            state.isLoading && state.expirations.isEmpty() ->
                YupanaLoadingState(modifier = Modifier.padding(horizontal = spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(DashboardIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    ) {
                        YupanaStatCard(
                            value = "${state.dueTodayCount}",
                            label = "Vencen hoy",
                            modifier = Modifier.weight(1f),
                            valueColor = colors.peligro,
                        )
                        YupanaStatCard(
                            value = "${state.dueSoonCount}",
                            label = "Próximos 7 días",
                            modifier = Modifier.weight(1f),
                            valueColor = colors.alerta,
                        )
                        YupanaStatCard(
                            value = "S/.${state.amountToCollect}",
                            label = "Por cobrar",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                item {
                    Text(
                        text = "Próximos a vencer",
                        style = YupanaTheme.typography.subtitle,
                        color = colors.textoPrincipal,
                    )
                }

                if (state.expirations.isEmpty()) {
                    item {
                        YupanaEmptyState(
                            title = "Aún no tienes asignaciones próximas",
                            message = "Crea tu primera asignación para empezar a hacer seguimiento.",
                            actionText = "Nueva asignación",
                            onAction = { onIntent(DashboardIntent.NewAssignmentClicked) },
                        )
                    }
                } else {
                    items(state.expirations, key = { it.assignmentId }) { expiration ->
                        YupanaAssignmentCard(
                            cliente = expiration.clientName,
                            subtitulo = "${expiration.serviceName} · ${expiration.profileLabel}",
                            precio = "${expiration.priceCharged ?: 0.0}",
                            estado = estadoFor(expiration.daysLeft),
                            estadoLabel = chipLabelFor(expiration.daysLeft),
                            logoText = expiration.serviceName.take(1).uppercase(),
                            logoColor = colors.marca,
                            modifier = Modifier.clickable {
                                onIntent(DashboardIntent.ExpirationClicked(expiration.assignmentId))
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun estadoFor(daysLeft: Int): YupanaEstado = when {
    daysLeft <= 0 -> YupanaEstado.Hoy
    daysLeft <= 7 -> YupanaEstado.Pronto
    else -> YupanaEstado.AlDia
}

private fun chipLabelFor(daysLeft: Int): String = when {
    daysLeft < 0 -> "Vencido"
    daysLeft == 0 -> "Hoy"
    daysLeft == 1 -> "Mañana"
    else -> "$daysLeft días"
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen(
        state = DashboardUiState(
            expirations = listOf(
                UpcomingExpiration(
                    assignmentId = "1", dueDate = "2026-07-01", daysLeft = 0, priceCharged = 20.0,
                    clientName = "María López", clientContact = null, profileLabel = "Perfil 2",
                    accountAlias = "Cuenta principal", serviceName = "Netflix",
                ),
            ),
        ),
        onIntent = {},
    )
}
