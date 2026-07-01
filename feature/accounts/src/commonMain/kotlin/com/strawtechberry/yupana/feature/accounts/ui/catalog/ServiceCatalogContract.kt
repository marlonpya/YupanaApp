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
    val showCreateDialog: Boolean = false,
    val newServiceName: String = "",
    val newServiceColor: String = servicePresetColors.first(),
    val isCreating: Boolean = false,
    val createError: String? = null,
) : UiState {
    val isCreateEnabled: Boolean get() = newServiceName.isNotBlank() && !isCreating
}

/** User actions on the service catalog. */
sealed interface ServiceCatalogIntent : UiIntent {
    data object Retry : ServiceCatalogIntent
    data class ServiceClicked(val id: String) : ServiceCatalogIntent
    data object AddServiceClicked : ServiceCatalogIntent
    data object DismissCreateDialog : ServiceCatalogIntent
    data class NewServiceNameChanged(val value: String) : ServiceCatalogIntent
    data class NewServiceColorChanged(val value: String) : ServiceCatalogIntent
    data object ConfirmCreateService : ServiceCatalogIntent
}

/** One-time effects of the service catalog. */
sealed interface ServiceCatalogEvent : UiEvent {
    data class ServiceSelected(val id: String) : ServiceCatalogEvent
}
