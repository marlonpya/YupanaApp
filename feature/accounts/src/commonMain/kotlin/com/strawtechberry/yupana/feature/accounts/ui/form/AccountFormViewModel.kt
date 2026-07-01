package com.strawtechberry.yupana.feature.accounts.ui.form

import androidx.lifecycle.viewModelScope
import com.strawtechberry.yupana.core.mvi.MviViewModel
import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import com.strawtechberry.yupana.feature.accounts.domain.usecase.CreateAccountUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetServicesUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.UpdateAccountUseCase
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage
import kotlinx.coroutines.launch

class AccountFormViewModel(
    accountId: String?,
    private val getAccount: GetAccountUseCase,
    private val getServices: GetServicesUseCase,
    private val createAccount: CreateAccountUseCase,
    private val updateAccount: UpdateAccountUseCase,
) : MviViewModel<AccountFormUiState, AccountFormIntent, AccountFormEvent>(AccountFormUiState(accountId = accountId)) {

    /** Cached once on load so [AccountFormIntent.ServiceSelected] can resolve name/color locally. */
    private var services: List<Service> = emptyList()

    init {
        if (accountId != null) setState { copy(isLoadingAccount = true) }
        viewModelScope.launch {
            services = getServices().getOrNull().orEmpty()
            if (accountId != null) {
                getAccount(accountId).fold(
                    onSuccess = { account ->
                        val service = services.find { it.id == account.serviceId }
                        setState {
                            copy(
                                isLoadingAccount = false,
                                serviceId = account.serviceId,
                                serviceName = service?.name,
                                serviceColor = service?.color,
                                alias = account.alias,
                                monthlyCost = account.monthlyCost?.toString().orEmpty(),
                                numProfiles = account.numProfiles.toString(),
                                billingDay = account.billingDay?.toString().orEmpty(),
                                notes = account.notes.orEmpty(),
                            )
                        }
                    },
                    onFailure = { error -> setState { copy(isLoadingAccount = false, formError = errorMessage(error)) } },
                )
            }
        }
    }

    override fun onIntent(intent: AccountFormIntent) {
        when (intent) {
            is AccountFormIntent.AliasChanged -> setState { copy(alias = intent.value, aliasError = null, formError = null) }
            is AccountFormIntent.MonthlyCostChanged -> setState { copy(monthlyCost = intent.value, formError = null) }
            is AccountFormIntent.NumProfilesChanged -> setState { copy(numProfiles = intent.value, formError = null) }
            is AccountFormIntent.BillingDayChanged -> setState { copy(billingDay = intent.value, formError = null) }
            is AccountFormIntent.NotesChanged -> setState { copy(notes = intent.value) }
            AccountFormIntent.PickServiceClicked -> sendEvent(AccountFormEvent.NavigateToServicePicker)
            is AccountFormIntent.ServiceSelected -> {
                val service = services.find { it.id == intent.serviceId }
                setState {
                    copy(
                        serviceId = intent.serviceId,
                        serviceName = service?.name,
                        serviceColor = service?.color,
                        serviceError = null,
                    )
                }
            }
            AccountFormIntent.Submit -> submit()
            AccountFormIntent.BackClicked -> sendEvent(AccountFormEvent.NavigateBack)
        }
    }

    private fun submit() {
        val state = currentState
        val serviceId = state.serviceId
        if (state.alias.isBlank() || serviceId == null) {
            setState {
                copy(
                    aliasError = if (alias.isBlank()) "Ingresa el alias de la cuenta" else null,
                    serviceError = if (serviceId == null) "Selecciona un servicio" else null,
                )
            }
            return
        }

        val monthlyCost = state.monthlyCost.trim().toDoubleOrNull()
        val numProfiles = state.numProfiles.trim().toIntOrNull()?.coerceAtLeast(1) ?: 1
        val billingDay = state.billingDay.trim().toIntOrNull()

        setState { copy(isLoading = true, formError = null) }
        viewModelScope.launch {
            val result = if (state.accountId != null) {
                updateAccount(state.accountId, serviceId, state.alias, monthlyCost, numProfiles, billingDay, state.notes)
            } else {
                createAccount(serviceId, state.alias, monthlyCost, numProfiles, billingDay, state.notes)
            }
            result.fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    sendEvent(AccountFormEvent.SavedSuccessfully)
                },
                onFailure = { error -> setState { copy(isLoading = false, formError = errorMessage(error)) } },
            )
        }
    }
}
