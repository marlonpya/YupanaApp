package com.strawtechberry.yupana.feature.settings.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaFilterChip
import com.strawtechberry.yupana.core.designsystem.component.YupanaSwitch
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.settings.domain.model.NotificationPreferences
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NotificationPreferencesScreen(
    state: NotificationPreferencesUiState,
    onIntent: (NotificationPreferencesIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(title = "Preferencias de notificación", onBack = { onIntent(NotificationPreferencesIntent.BackClicked) })

        when {
            state.isLoading -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null && !state.isSaving -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(NotificationPreferencesIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> Column(
                modifier = Modifier.fillMaxSize().padding(spacing.lg),
                verticalArrangement = Arrangement.spacedBy(spacing.lg),
            ) {
                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Notificaciones activas", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
                        YupanaSwitch(
                            checked = state.enabled,
                            onCheckedChange = { onIntent(NotificationPreferencesIntent.EnabledChanged(it)) },
                        )
                    }
                }

                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Días antes del vencimiento para avisar",
                        style = YupanaTheme.typography.body,
                        color = colors.textoPrincipal,
                    )
                    Spacer(Modifier.height(spacing.md))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        items(NotificationPreferences.DAYS_BEFORE_OPTIONS) { day ->
                            YupanaFilterChip(
                                text = if (day == 1) "1 día" else "$day días",
                                selected = state.daysBefore.contains(day),
                                onClick = { onIntent(NotificationPreferencesIntent.DayToggled(day)) },
                            )
                        }
                    }
                }

                YupanaCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Hora del recordatorio", style = YupanaTheme.typography.body, color = colors.textoPrincipal)
                    Spacer(Modifier.height(spacing.md))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onIntent(NotificationPreferencesIntent.HourDecremented) }) {
                            Text("−", style = YupanaTheme.typography.title, color = colors.marca)
                        }
                        Text(
                            text = "${state.reminderHour.toString().padStart(2, '0')}:00",
                            style = YupanaTheme.typography.title,
                            color = colors.acento,
                            modifier = Modifier.width(72.dp),
                            textAlign = TextAlign.Center,
                        )
                        IconButton(onClick = { onIntent(NotificationPreferencesIntent.HourIncremented) }) {
                            Text("+", style = YupanaTheme.typography.title, color = colors.marca)
                        }
                    }
                }

                if (state.error != null && state.isSaving.not()) {
                    Text(state.error, color = colors.peligro, style = YupanaTheme.typography.body)
                }
                if (state.saved) {
                    Text("Preferencias guardadas", color = colors.exito, style = YupanaTheme.typography.body)
                }

                YupanaButton(
                    text = "Guardar",
                    onClick = { onIntent(NotificationPreferencesIntent.SaveClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    loading = state.isSaving,
                )
            }
        }
    }
}

@Preview
@Composable
private fun NotificationPreferencesScreenPreview() {
    NotificationPreferencesScreen(
        state = NotificationPreferencesUiState(isLoading = false, enabled = true, daysBefore = setOf(1, 3, 7), reminderHour = 9),
        onIntent = {},
    )
}
