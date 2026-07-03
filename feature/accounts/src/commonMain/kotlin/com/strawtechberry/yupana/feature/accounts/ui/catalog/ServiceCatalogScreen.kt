package com.strawtechberry.yupana.feature.accounts.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaServiceLogo
import com.strawtechberry.yupana.core.designsystem.component.YupanaTextField
import com.strawtechberry.yupana.core.designsystem.component.YupanaTopBar
import com.strawtechberry.yupana.core.designsystem.state.YupanaEmptyState
import com.strawtechberry.yupana.core.designsystem.state.YupanaErrorState
import com.strawtechberry.yupana.core.designsystem.state.YupanaLoadingState
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import com.strawtechberry.yupana.feature.accounts.ui.common.parseServiceColor
import com.strawtechberry.yupana.feature.accounts.ui.common.servicePresetColors
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Service catalog screen (stateless): browses global + own services, can create/edit/delete a custom one. */
@Composable
fun ServiceCatalogScreen(
    picker: Boolean,
    state: ServiceCatalogUiState,
    onIntent: (ServiceCatalogIntent) -> Unit,
    onBack: () -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(colors.fondo).navigationBarsPadding()) {
        YupanaTopBar(
            title = if (picker) "Selecciona un servicio" else "Catálogo de servicios",
            onBack = onBack,
            action = {
                IconButton(onClick = { onIntent(ServiceCatalogIntent.AddServiceClicked) }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Agregar servicio", tint = colors.marca)
                }
            },
        )

        when {
            state.isLoading && state.services.isEmpty() -> YupanaLoadingState(modifier = Modifier.padding(spacing.lg))
            state.error != null -> YupanaErrorState(
                message = state.error,
                onRetry = { onIntent(ServiceCatalogIntent.Retry) },
                modifier = Modifier.padding(spacing.lg),
            )
            state.services.isEmpty() -> YupanaEmptyState(
                title = "Sin servicios todavía",
                message = "Agrega un servicio propio para empezar a crear cuentas.",
                actionText = "Agregar servicio",
                onAction = { onIntent(ServiceCatalogIntent.AddServiceClicked) },
                modifier = Modifier.padding(spacing.lg),
            )
            else -> {
                val globalServices = state.services.filter { it.isGlobal }
                val ownServices = state.services.filter { !it.isGlobal }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = spacing.lg, vertical = spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    if (globalServices.isNotEmpty()) {
                        item { SectionLabel("Globales") }
                        items(globalServices, key = { it.id }) { service ->
                            ServiceRow(
                                service = service,
                                onClick = { onIntent(ServiceCatalogIntent.ServiceClicked(service.id)) },
                                onEdit = null,
                                onDelete = null,
                            )
                        }
                    }
                    if (ownServices.isNotEmpty()) {
                        item { SectionLabel("Tus servicios") }
                        items(ownServices, key = { it.id }) { service ->
                            ServiceRow(
                                service = service,
                                onClick = { onIntent(ServiceCatalogIntent.ServiceClicked(service.id)) },
                                onEdit = { onIntent(ServiceCatalogIntent.EditServiceClicked(service)) },
                                onDelete = { onIntent(ServiceCatalogIntent.DeleteServiceClicked(service.id)) },
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showFormDialog) {
        ServiceFormDialog(state = state, onIntent = onIntent)
    }

    if (state.deletingServiceId != null) {
        DeleteServiceDialog(state = state, onIntent = onIntent)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = YupanaTheme.typography.label,
        color = YupanaTheme.colors.textoSecundario,
        modifier = Modifier.padding(top = YupanaTheme.spacing.sm),
    )
}

@Composable
private fun ServiceRow(
    service: Service,
    onClick: () -> Unit,
    onEdit: (() -> Unit)?,
    onDelete: (() -> Unit)?,
) {
    val colors = YupanaTheme.colors
    YupanaCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            YupanaServiceLogo(
                text = service.name.take(1).uppercase(),
                color = parseServiceColor(service.color) ?: colors.marca,
            )
            Spacer(Modifier.width(YupanaTheme.spacing.md))
            Text(
                service.name,
                style = YupanaTheme.typography.subtitle,
                color = colors.textoPrincipal,
                modifier = Modifier.weight(1f),
            )
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Editar servicio", tint = colors.textoSecundario)
                }
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Eliminar servicio", tint = colors.peligro)
                }
            }
        }
    }
}

@Composable
private fun ServiceFormDialog(
    state: ServiceCatalogUiState,
    onIntent: (ServiceCatalogIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val isEditing = state.editingServiceId != null
    AlertDialog(
        onDismissRequest = { onIntent(ServiceCatalogIntent.DismissFormDialog) },
        title = { Text(if (isEditing) "Editar servicio" else "Nuevo servicio", color = colors.textoPrincipal) },
        text = {
            Column {
                YupanaTextField(
                    value = state.formName,
                    onValueChange = { onIntent(ServiceCatalogIntent.FormNameChanged(it)) },
                    label = "Nombre",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(spacing.md))
                Text("Color", style = YupanaTheme.typography.label, color = colors.textoSecundario)
                Spacer(Modifier.height(spacing.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    servicePresetColors.forEach { hex ->
                        val swatchColor = parseServiceColor(hex) ?: colors.marca
                        val selected = state.formColor == hex
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(swatchColor)
                                .then(
                                    if (selected) Modifier.border(2.dp, colors.textoPrincipal, CircleShape)
                                    else Modifier
                                )
                                .clickable { onIntent(ServiceCatalogIntent.FormColorChanged(hex)) },
                        )
                    }
                }
                if (state.formError != null) {
                    Spacer(Modifier.height(spacing.md))
                    Text(state.formError, color = colors.peligro, style = YupanaTheme.typography.body, textAlign = TextAlign.Center)
                }
            }
        },
        confirmButton = {
            YupanaButton(
                text = if (isEditing) "Guardar" else "Crear",
                onClick = { onIntent(ServiceCatalogIntent.ConfirmSaveService) },
                enabled = state.isFormEnabled,
                loading = state.isSavingForm,
            )
        },
        dismissButton = {
            YupanaButton(
                text = "Cancelar",
                variant = YupanaButtonVariant.Text,
                onClick = { onIntent(ServiceCatalogIntent.DismissFormDialog) },
            )
        },
    )
}

@Composable
private fun DeleteServiceDialog(
    state: ServiceCatalogUiState,
    onIntent: (ServiceCatalogIntent) -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    AlertDialog(
        onDismissRequest = { onIntent(ServiceCatalogIntent.DismissDeleteConfirm) },
        title = { Text("Eliminar servicio", color = colors.textoPrincipal) },
        text = {
            Column {
                Text(
                    "¿Seguro que quieres eliminar este servicio? Esta acción no se puede deshacer.",
                    color = colors.textoSecundario,
                )
                if (state.deleteError != null) {
                    Spacer(Modifier.height(spacing.md))
                    Text(state.deleteError, color = colors.peligro, style = YupanaTheme.typography.body, textAlign = TextAlign.Center)
                }
            }
        },
        confirmButton = {
            YupanaButton(
                text = "Eliminar",
                variant = YupanaButtonVariant.Destructive,
                onClick = { onIntent(ServiceCatalogIntent.ConfirmDeleteService) },
                loading = state.isDeleting,
            )
        },
        dismissButton = {
            YupanaButton(
                text = "Cancelar",
                variant = YupanaButtonVariant.Text,
                onClick = { onIntent(ServiceCatalogIntent.DismissDeleteConfirm) },
            )
        },
    )
}

@Preview
@Composable
private fun ServiceCatalogScreenPreview() {
    ServiceCatalogScreen(
        picker = false,
        state = ServiceCatalogUiState(
            services = listOf(
                Service(id = "1", name = "Netflix", color = "#E50914", logoUrl = null, isGlobal = true),
                Service(id = "2", name = "Mi servicio", color = "#4E8FE5", logoUrl = null, isGlobal = false),
            ),
        ),
        onIntent = {},
        onBack = {},
    )
}
