package com.strawtechberry.yupana.feature.dashboard.ui.allexpirations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaAssignmentCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaFilterChip
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import com.strawtechberry.yupana.feature.dashboard.ui.common.chipLabelFor
import com.strawtechberry.yupana.feature.dashboard.ui.common.estadoFor
import org.jetbrains.compose.ui.tooling.preview.Preview

/** "Todos los vencimientos" screen (stateless): filtros + búsqueda + lista completa. */
@Composable
fun AllExpirationsScreen(
    state: AllExpirationsUiState,
    onIntent: (AllExpirationsIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = "Todos los vencimientos",
            onBack = { onIntent(AllExpirationsIntent.BackClicked) },
        )

        YupanaTextField(
            value = state.query,
            onValueChange = { onIntent(AllExpirationsIntent.QueryChanged(it)) },
            label = "Buscar cliente",
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.lg, vertical = spacing.sm),
            leadingIcon = Icons.Rounded.Search,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            items(ExpirationFilter.entries) { filter ->
                YupanaFilterChip(
                    text = filterLabel(filter),
                    selected = filter == state.filter,
                    onClick = { onIntent(AllExpirationsIntent.FilterChanged(filter)) },
                )
            }
        }

        when {
            state.isLoading && state.expirations.isEmpty() ->
                YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(AllExpirationsIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.visibleExpirations.isEmpty() -> YupanaEmptyState(
                title = "Sin vencimientos",
                message = "No hay asignaciones que coincidan con este filtro.",
                modifier = Modifier.padding(spacing.lg),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                items(state.visibleExpirations, key = { it.assignmentId }) { expiration ->
                    YupanaAssignmentCard(
                        cliente = expiration.clientName,
                        subtitulo = "${expiration.serviceName} · ${expiration.profileLabel}",
                        precio = "${expiration.priceCharged ?: 0.0}",
                        estado = estadoFor(expiration.daysLeft),
                        estadoLabel = chipLabelFor(expiration.daysLeft),
                        logoText = expiration.serviceName.take(1).uppercase(),
                        logoColor = colors.marca,
                        modifier = Modifier.clickable {
                            onIntent(AllExpirationsIntent.ExpirationClicked(expiration.assignmentId))
                        },
                    )
                }
            }
        }
    }
}

private fun filterLabel(filter: ExpirationFilter): String = when (filter) {
    ExpirationFilter.Hoy -> "Hoy"
    ExpirationFilter.Proximos7Dias -> "Próximos 7 días"
    ExpirationFilter.EsteMes -> "Este mes"
    ExpirationFilter.Vencidos -> "Vencidos"
    ExpirationFilter.Todos -> "Todos"
}

@Preview
@Composable
private fun AllExpirationsScreenPreview() {
    AllExpirationsScreen(
        state = AllExpirationsUiState(
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
