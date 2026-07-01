package com.strawtechberry.yupana.feature.accounts.ui.detail

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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Account detail screen (stateless): account summary + its profiles. */
@Composable
fun AccountDetailScreen(
    state: AccountDetailUiState,
    onIntent: (AccountDetailIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo)) {
        YupanaTopBar(
            title = state.account?.alias ?: "Cuenta",
            onBack = { onIntent(AccountDetailIntent.BackClicked) },
            action = {
                IconButton(onClick = { onIntent(AccountDetailIntent.EditClicked) }) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Editar cuenta", tint = colors.marca)
                }
            },
        )

        when {
            state.isLoading && state.account == null -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(AccountDetailIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.account != null -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                item { AccountSummaryCard(state.account) }
                item {
                    Text(
                        text = "Perfiles",
                        style = YupanaTheme.typography.label,
                        color = colors.textoSecundario,
                        modifier = Modifier.padding(top = spacing.sm),
                    )
                }
                items(state.profiles, key = { it.id }) { profile ->
                    ProfileListItem(
                        profile = profile,
                        onAssignClicked = { onIntent(AccountDetailIntent.AssignClicked(profile.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountSummaryCard(account: Account) {
    val colors = YupanaTheme.colors
    YupanaCard(modifier = Modifier.fillMaxWidth()) {
        account.monthlyCost?.let {
            Text("Costo mensual: S/. $it", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
        }
        account.billingDay?.let {
            Text("Día de corte: $it", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
        }
        Text("Perfiles: ${account.numProfiles}", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
        account.notes?.takeIf { it.isNotBlank() }?.let {
            Text(it, style = YupanaTheme.typography.caption, color = colors.textoSecundario)
        }
    }
}

@Preview
@Composable
private fun AccountDetailScreenPreview() {
    val account = Account(
        id = "a1", serviceId = "1", alias = "Cuenta principal", monthlyCost = 45.0,
        numProfiles = 3, billingDay = 15, notes = "Compartida con la familia", createdAt = null,
    )
    AccountDetailScreen(
        state = AccountDetailUiState(
            accountId = "a1",
            account = account,
            profiles = listOf(
                Profile(id = "p1", accountId = "a1", label = "Perfil 1", occupant = null),
                Profile(id = "p2", accountId = "a1", label = "Perfil 2", occupant = null),
            ),
        ),
        onIntent = {},
    )
}
