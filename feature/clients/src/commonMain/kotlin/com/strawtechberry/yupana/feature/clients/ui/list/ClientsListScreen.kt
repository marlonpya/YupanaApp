package com.strawtechberry.yupana.feature.clients.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.clients.domain.model.Client
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Clients list screen (stateless). Receives the state and emits intents via [onIntent]. */
@Composable
fun ClientsListScreen(
    state: ClientsListUiState,
    onIntent: (ClientsListIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = "Clientes",
            action = {
                IconButton(onClick = { onIntent(ClientsListIntent.CreateClicked) }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Crear cliente", tint = colors.marca)
                }
            },
        )

        YupanaTextField(
            value = state.query,
            onValueChange = { onIntent(ClientsListIntent.QueryChanged(it)) },
            label = "Buscar cliente",
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.lg, vertical = spacing.sm),
            leadingIcon = Icons.Rounded.Search,
        )

        when {
            state.isLoading && state.clients.isEmpty() -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(ClientsListIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.clients.isEmpty() -> YupanaEmptyState(
                title = "Aún no tienes clientes",
                message = "Registra tu primer cliente para empezar a asignarle perfiles.",
                actionText = "Crear cliente",
                onAction = { onIntent(ClientsListIntent.CreateClicked) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                items(state.clients, key = { it.id }) { client ->
                    ClientListItem(client = client, onClick = { onIntent(ClientsListIntent.ClientClicked(client.id)) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClientsListScreenPreview() {
    ClientsListScreen(
        state = ClientsListUiState(
            clients = listOf(
                Client(id = "1", name = "María López", contact = "+51 999 999 999", notes = null, createdAt = null),
                Client(id = "2", name = "Carlos Ruiz", contact = null, notes = null, createdAt = null),
            ),
        ),
        onIntent = {},
    )
}
