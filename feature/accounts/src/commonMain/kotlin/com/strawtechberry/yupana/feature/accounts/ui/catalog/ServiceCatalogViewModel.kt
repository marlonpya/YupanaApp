package com.strawtechberry.yupana.feature.accounts.ui.catalog

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.usecase.CreateServiceUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.DeleteServiceUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetServicesUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.UpdateServiceUseCase
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage
import com.strawtechberry.yupana.feature.accounts.ui.common.servicePresetColors
import kotlinx.coroutines.launch

class ServiceCatalogViewModel(
    private val getServices: GetServicesUseCase,
    private val createService: CreateServiceUseCase,
    private val updateService: UpdateServiceUseCase,
    private val deleteService: DeleteServiceUseCase,
) : MviViewModel<ServiceCatalogUiState, ServiceCatalogIntent, ServiceCatalogEvent>(ServiceCatalogUiState()) {

    override fun onIntent(intent: ServiceCatalogIntent) {
        when (intent) {
            ServiceCatalogIntent.Retry -> load()
            is ServiceCatalogIntent.ServiceClicked -> sendEvent(ServiceCatalogEvent.ServiceSelected(intent.id))
            ServiceCatalogIntent.AddServiceClicked -> setState {
                copy(
                    showFormDialog = true,
                    editingServiceId = null,
                    formName = "",
                    formColor = servicePresetColors.first(),
                    formError = null,
                )
            }
            is ServiceCatalogIntent.EditServiceClicked -> setState {
                copy(
                    showFormDialog = true,
                    editingServiceId = intent.service.id,
                    formName = intent.service.name,
                    formColor = intent.service.color ?: servicePresetColors.first(),
                    formError = null,
                )
            }
            ServiceCatalogIntent.DismissFormDialog -> setState {
                copy(showFormDialog = false, editingServiceId = null, formName = "", formError = null)
            }
            is ServiceCatalogIntent.FormNameChanged -> setState { copy(formName = intent.value, formError = null) }
            is ServiceCatalogIntent.FormColorChanged -> setState { copy(formColor = intent.value) }
            ServiceCatalogIntent.ConfirmSaveService -> confirmSaveService()
            is ServiceCatalogIntent.DeleteServiceClicked -> setState { copy(deletingServiceId = intent.id, deleteError = null) }
            ServiceCatalogIntent.DismissDeleteConfirm -> setState { copy(deletingServiceId = null, deleteError = null) }
            ServiceCatalogIntent.ConfirmDeleteService -> confirmDeleteService()
        }
    }

    /** Loads the catalog; called from the Route each time it re-enters composition. */
    fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getServices().fold(
                onSuccess = { services -> setState { copy(isLoading = false, services = services) } },
                onFailure = { error -> setState { copy(isLoading = false, error = errorMessage(error)) } },
            )
        }
    }

    private fun confirmSaveService() {
        val state = currentState
        val editingId = state.editingServiceId
        setState { copy(isSavingForm = true, formError = null) }
        viewModelScope.launch {
            val result = if (editingId != null) {
                updateService(editingId, state.formName, state.formColor)
            } else {
                createService(state.formName, state.formColor)
            }
            result.fold(
                onSuccess = { saved ->
                    setState {
                        copy(
                            isSavingForm = false,
                            showFormDialog = false,
                            editingServiceId = null,
                            formName = "",
                            services = if (editingId != null) services.map { if (it.id == saved.id) saved else it } else services + saved,
                        )
                    }
                },
                onFailure = { error -> setState { copy(isSavingForm = false, formError = errorMessage(error)) } },
            )
        }
    }

    private fun confirmDeleteService() {
        val id = currentState.deletingServiceId ?: return
        setState { copy(isDeleting = true, deleteError = null) }
        viewModelScope.launch {
            deleteService(id).fold(
                onSuccess = {
                    setState {
                        copy(isDeleting = false, deletingServiceId = null, services = services.filterNot { it.id == id })
                    }
                },
                onFailure = { error -> setState { copy(isDeleting = false, deleteError = errorMessage(error)) } },
            )
        }
    }
}
