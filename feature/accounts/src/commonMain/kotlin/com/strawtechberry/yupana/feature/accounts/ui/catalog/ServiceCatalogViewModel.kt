package com.strawtechberry.yupana.feature.accounts.ui.catalog

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.usecase.CreateServiceUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetServicesUseCase
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage
import kotlinx.coroutines.launch

class ServiceCatalogViewModel(
    private val getServices: GetServicesUseCase,
    private val createService: CreateServiceUseCase,
) : MviViewModel<ServiceCatalogUiState, ServiceCatalogIntent, ServiceCatalogEvent>(ServiceCatalogUiState()) {

    override fun onIntent(intent: ServiceCatalogIntent) {
        when (intent) {
            ServiceCatalogIntent.Retry -> load()
            is ServiceCatalogIntent.ServiceClicked -> sendEvent(ServiceCatalogEvent.ServiceSelected(intent.id))
            ServiceCatalogIntent.AddServiceClicked -> setState { copy(showCreateDialog = true, createError = null) }
            ServiceCatalogIntent.DismissCreateDialog -> setState {
                copy(showCreateDialog = false, newServiceName = "", createError = null)
            }
            is ServiceCatalogIntent.NewServiceNameChanged -> setState { copy(newServiceName = intent.value, createError = null) }
            is ServiceCatalogIntent.NewServiceColorChanged -> setState { copy(newServiceColor = intent.value) }
            ServiceCatalogIntent.ConfirmCreateService -> confirmCreateService()
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

    private fun confirmCreateService() {
        val state = currentState
        setState { copy(isCreating = true, createError = null) }
        viewModelScope.launch {
            createService(state.newServiceName, state.newServiceColor).fold(
                onSuccess = { service ->
                    setState {
                        copy(
                            isCreating = false,
                            showCreateDialog = false,
                            newServiceName = "",
                            services = services + service,
                        )
                    }
                },
                onFailure = { error ->
                    setState { copy(isCreating = false, createError = errorMessage(error)) }
                },
            )
        }
    }
}
