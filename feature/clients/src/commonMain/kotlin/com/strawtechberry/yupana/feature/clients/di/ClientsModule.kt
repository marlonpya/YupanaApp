package com.strawtechberry.yupana.feature.clients.di

import com.strawtechberry.yupana.feature.clients.data.DefaultClientRepository
import com.strawtechberry.yupana.feature.clients.domain.ClientRepository
import com.strawtechberry.yupana.feature.clients.domain.usecase.CreateClientUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.GetClientsUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.SearchClientsUseCase
import com.strawtechberry.yupana.feature.clients.domain.usecase.UpdateClientUseCase
import com.strawtechberry.yupana.feature.clients.ui.form.ClientFormViewModel
import com.strawtechberry.yupana.feature.clients.ui.list.ClientsListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Clients Koin module. Postgrest is provided by `supabaseModule` (:core:supabase),
 * so this module must be loaded alongside it.
 */
val clientsModule = module {
    single<ClientRepository> { DefaultClientRepository(get()) }

    factoryOf(::GetClientsUseCase)
    factoryOf(::SearchClientsUseCase)
    factoryOf(::GetClientUseCase)
    factoryOf(::CreateClientUseCase)
    factoryOf(::UpdateClientUseCase)

    viewModelOf(::ClientsListViewModel)
    viewModel { (clientId: String?) -> ClientFormViewModel(clientId, get(), get(), get()) }
}
