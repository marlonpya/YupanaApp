package com.strawtechberry.yupana.feature.accounts.ui.catalog

import com.strawtechberry.yupana.core.mvi.UiEvent
import com.strawtechberry.yupana.core.mvi.UiIntent
import com.strawtechberry.yupana.core.mvi.UiState
import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import com.strawtechberry.yupana.feature.accounts.ui.common.servicePresetColors

/** Service catalog screen state. */
data class ServiceCatalogUiState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    // Create/edit dialog: shared by both flows, `editingServiceId == null` means "creating".
    val showFormDialog: Boolean = false,
    val editingServiceId: String? = null,
    val formName: String = "",
    val formColor: String = servicePresetColors.first(),
    val isSavingForm: Boolean = false,
    val formError: String? = null,
    // Delete confirmation.
    val deletingServiceId: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
) : UiState {
    val isFormEnabled: Boolean get() = formName.isNotBlank() && !isSavingForm
}

/** User actions on the service catalog. */
sealed interface ServiceCatalogIntent : UiIntent {
    data object Retry : ServiceCatalogIntent
    data class ServiceClicked(val id: String) : ServiceCatalogIntent
    data object AddServiceClicked : ServiceCatalogIntent
    data class EditServiceClicked(val service: Service) : ServiceCatalogIntent
    data object DismissFormDialog : ServiceCatalogIntent
    data class FormNameChanged(val value: String) : ServiceCatalogIntent
    data class FormColorChanged(val value: String) : ServiceCatalogIntent
    data object ConfirmSaveService : ServiceCatalogIntent
    data class DeleteServiceClicked(val id: String) : ServiceCatalogIntent
    data object DismissDeleteConfirm : ServiceCatalogIntent
    data object ConfirmDeleteService : ServiceCatalogIntent
}

/** One-time effects of the service catalog. */
sealed interface ServiceCatalogEvent : UiEvent {
    data class ServiceSelected(val id: String) : ServiceCatalogEvent
}
