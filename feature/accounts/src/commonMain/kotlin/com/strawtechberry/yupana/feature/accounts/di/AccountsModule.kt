package com.strawtechberry.yupana.feature.accounts.di

import com.strawtechberry.yupana.feature.accounts.data.DefaultAccountRepository
import com.strawtechberry.yupana.feature.accounts.data.DefaultServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.usecase.CreateAccountUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.CreateServiceUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.DeleteServiceUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountGroupsUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountProfilesUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetServicesUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.UpdateAccountUseCase
import com.strawtechberry.yupana.feature.accounts.domain.usecase.UpdateServiceUseCase
import com.strawtechberry.yupana.feature.accounts.ui.catalog.ServiceCatalogViewModel
import com.strawtechberry.yupana.feature.accounts.ui.detail.AccountDetailViewModel
import com.strawtechberry.yupana.feature.accounts.ui.form.AccountFormViewModel
import com.strawtechberry.yupana.feature.accounts.ui.list.AccountsListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Accounts Koin module. Postgrest/Auth are provided by `supabaseModule`
 * (:core:supabase), so this module must be loaded alongside it.
 */
val accountsModule = module {
    single<AccountRepository> { DefaultAccountRepository(get()) }
    single<ServiceRepository> { DefaultServiceRepository(get(), get()) }

    factoryOf(::GetServicesUseCase)
    factoryOf(::CreateServiceUseCase)
    factoryOf(::UpdateServiceUseCase)
    factoryOf(::DeleteServiceUseCase)
    factoryOf(::GetAccountGroupsUseCase)
    factoryOf(::GetAccountUseCase)
    factoryOf(::GetAccountProfilesUseCase)
    factoryOf(::CreateAccountUseCase)
    factoryOf(::UpdateAccountUseCase)

    viewModelOf(::AccountsListViewModel)
    viewModel { (accountId: String) -> AccountDetailViewModel(accountId, get(), get()) }
    viewModel { (accountId: String?) -> AccountFormViewModel(accountId, get(), get(), get(), get()) }
    viewModelOf(::ServiceCatalogViewModel)
}
