package com.strawtechberry.yupana.feature.accounts.ui.list

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
import androidx.compose.material.icons.rounded.List
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
import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountWithOccupancy
import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import org.jetbrains.compose.ui.tooling.preview.Preview

/** "Mis cuentas" screen (stateless): accounts grouped by service, expand/collapse. */
@Composable
fun AccountsListScreen(
    state: AccountsListUiState,
    onIntent: (AccountsListIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = "Mis cuentas",
            action = {
                IconButton(onClick = { onIntent(AccountsListIntent.CatalogClicked) }) {
                    Icon(Icons.Rounded.List, contentDescription = "Catálogo de servicios", tint = colors.textoSecundario)
                }
                IconButton(onClick = { onIntent(AccountsListIntent.CreateClicked) }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Crear cuenta", tint = colors.marca)
                }
            },
        )

        YupanaTextField(
            value = state.query,
            onValueChange = { onIntent(AccountsListIntent.QueryChanged(it)) },
            label = "Buscar por alias o servicio",
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.lg, vertical = spacing.sm),
            leadingIcon = Icons.Rounded.Search,
        )

        when {
            state.isLoading && state.groups.isEmpty() -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(AccountsListIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.groups.isEmpty() -> YupanaEmptyState(
                title = "Aún no tienes cuentas",
                message = "Crea tu primera cuenta para empezar a asignar perfiles.",
                actionText = "Crear cuenta",
                onAction = { onIntent(AccountsListIntent.CreateClicked) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                state.groups.forEach { group ->
                    val expanded = group.service.id in state.expandedServiceIds
                    item(key = group.service.id) {
                        AccountGroupHeader(
                            group = group,
                            expanded = expanded,
                            onClick = { onIntent(AccountsListIntent.GroupClicked(group.service.id)) },
                        )
                    }
                    if (expanded) {
                        items(group.accounts, key = { it.account.id }) { accountWithOccupancy ->
                            AccountListItem(
                                accountWithOccupancy = accountWithOccupancy,
                                onClick = { onIntent(AccountsListIntent.AccountClicked(accountWithOccupancy.account.id)) },
                                modifier = Modifier.padding(start = spacing.lg),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountsListScreenPreview() {
    val service = Service(id = "1", name = "Netflix", color = "#E50914", logoUrl = null, isGlobal = true)
    val account = Account(
        id = "a1", serviceId = "1", alias = "Cuenta principal", monthlyCost = 45.0,
        numProfiles = 5, billingDay = 15, notes = null, createdAt = null,
    )
    AccountsListScreen(
        state = AccountsListUiState(
            groups = listOf(AccountGroup(service, listOf(AccountWithOccupancy(account, 3)))),
            expandedServiceIds = setOf("1"),
        ),
        onIntent = {},
    )
}
