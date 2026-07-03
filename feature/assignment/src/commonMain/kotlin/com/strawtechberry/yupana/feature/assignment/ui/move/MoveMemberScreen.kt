package com.strawtechberry.yupana.feature.assignment.ui.move

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountWithOccupancy
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview

/** "Mover integrante" screen (stateless): elige cuenta destino (mismo servicio, con cupo) y perfil libre. */
@Composable
fun MoveMemberScreen(
    state: MoveMemberUiState,
    onIntent: (MoveMemberIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(
            title = state.clientName?.let { "Mover a $it" } ?: "Mover integrante",
            onBack = { onIntent(MoveMemberIntent.BackClicked) },
        )

        when {
            state.isLoading && state.sourceServiceName == null -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null && state.sourceServiceName == null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(MoveMemberIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                item {
                    Text(
                        text = "Desde ${state.sourceServiceName} · ${state.sourceAccountAlias} · ${state.sourceProfileLabel}",
                        style = YupanaTheme.typography.body,
                        color = colors.textoSecundario,
                    )
                    Spacer(Modifier.height(spacing.md))
                }

                if (state.selectedAccountId == null) {
                    item {
                        Text("Elige cuenta destino", style = YupanaTheme.typography.label, color = colors.textoSecundario)
                        Spacer(Modifier.height(spacing.sm))
                    }
                    if (state.candidateAccounts.isEmpty()) {
                        item {
                            YupanaEmptyState(
                                title = "No tienes otras cuentas de ${state.sourceServiceName} con cupo",
                                message = "Considera crear una nueva cuenta para poder mover a este cliente.",
                                actionText = "Crear cuenta",
                                onAction = { onIntent(MoveMemberIntent.CreateAccountClicked) },
                            )
                        }
                    } else {
                        items(state.candidateAccounts, key = { it.account.id }) { candidate ->
                            CandidateAccountItem(
                                candidate = candidate,
                                onClick = { onIntent(MoveMemberIntent.AccountSelected(candidate.account.id, candidate.account.alias)) },
                            )
                        }
                    }
                } else {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Cuenta destino: ${state.selectedAccountAlias}",
                                style = YupanaTheme.typography.label,
                                color = colors.textoSecundario,
                            )
                            Text(
                                text = "Cambiar",
                                style = YupanaTheme.typography.label,
                                color = colors.acento,
                                modifier = Modifier.clickable {
                                    onIntent(MoveMemberIntent.ChangeAccountClicked)
                                },
                            )
                        }
                        Spacer(Modifier.height(spacing.sm))
                    }

                    if (state.isLoadingProfiles) {
                        item { YupanaLoadingState() }
                    } else if (state.availableProfiles.isEmpty()) {
                        item {
                            YupanaEmptyState(
                                title = "Sin perfiles libres",
                                message = "Esta cuenta ya no tiene cupo. Elige otra cuenta destino.",
                            )
                        }
                    } else {
                        items(state.availableProfiles, key = { it.id }) { profile ->
                            CandidateProfileItem(
                                profile = profile,
                                selected = profile.id == state.selectedProfileId,
                                onClick = { onIntent(MoveMemberIntent.ProfileSelected(profile.id, profile.label)) },
                            )
                        }
                    }
                }

                if (state.error != null) {
                    item {
                        Text(
                            text = state.error,
                            style = YupanaTheme.typography.body,
                            color = colors.peligro,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(spacing.md))
                    YupanaButton(
                        text = "Mover",
                        onClick = { onIntent(MoveMemberIntent.MoveClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.canMove,
                        loading = state.isMoving,
                    )
                }
            }
        }
    }

    if (state.showConfirm) {
        AlertDialog(
            onDismissRequest = { onIntent(MoveMemberIntent.DismissConfirm) },
            title = { Text("Mover integrante", color = colors.textoPrincipal) },
            text = {
                Text(
                    text = "Se conserva el precio (S/. ${state.priceCharged ?: 0.0}) y el vencimiento actual " +
                        "(${state.dueDate ?: "-"}). Solo cambia de cuenta.",
                    color = colors.textoSecundario,
                )
            },
            confirmButton = {
                YupanaButton(
                    text = "Mover",
                    onClick = { onIntent(MoveMemberIntent.ConfirmMove) },
                    loading = state.isMoving,
                )
            },
            dismissButton = {
                YupanaButton(
                    text = "Cancelar",
                    variant = YupanaButtonVariant.Text,
                    onClick = { onIntent(MoveMemberIntent.DismissConfirm) },
                )
            },
        )
    }
}

@Composable
private fun CandidateAccountItem(candidate: AccountWithOccupancy, onClick: () -> Unit) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val free = candidate.totalProfiles - candidate.occupiedProfiles

    YupanaCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(candidate.account.alias, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
        Row {
            candidate.account.billingDay?.let {
                Text("Día de corte: $it", style = YupanaTheme.typography.caption, color = colors.textoSecundario)
                Spacer(Modifier.width(spacing.sm))
            }
            Text("$free libres", style = YupanaTheme.typography.caption, color = colors.exito)
        }
    }
}

@Composable
private fun CandidateProfileItem(profile: Profile, selected: Boolean, onClick: () -> Unit) {
    val colors = YupanaTheme.colors
    YupanaCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(
            text = profile.label,
            style = YupanaTheme.typography.subtitle,
            color = if (selected) colors.acento else colors.textoPrincipal,
        )
    }
}

@Preview
@Composable
private fun MoveMemberScreenPreview() {
    MoveMemberScreen(
        state = MoveMemberUiState(
            assignmentId = "1",
            clientName = "María López",
            sourceServiceName = "Netflix",
            sourceAccountAlias = "Cuenta principal",
            sourceProfileLabel = "Perfil 2",
            priceCharged = 20.0,
            dueDate = "2026-08-01",
            candidateAccounts = listOf(
                AccountWithOccupancy(
                    account = Account(
                        id = "a2", serviceId = "s1", alias = "Cuenta secundaria", monthlyCost = 45.0,
                        numProfiles = 5, billingDay = 10, notes = null, createdAt = null,
                    ),
                    occupiedProfiles = 2,
                ),
            ),
        ),
        onIntent = {},
    )
}
