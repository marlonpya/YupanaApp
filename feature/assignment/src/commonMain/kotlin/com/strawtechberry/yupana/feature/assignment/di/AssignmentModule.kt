package com.strawtechberry.yupana.feature.assignment.di

import com.strawtechberry.yupana.feature.assignment.data.DefaultAssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.AssignmentRepository
import com.strawtechberry.yupana.feature.assignment.domain.usecase.AssignProfileToClientUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.EditAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetAvailableProfilesUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.GetUpcomingExpirationsUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.LiberateAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.domain.usecase.RenewAssignmentUseCase
import com.strawtechberry.yupana.feature.assignment.ui.assign.AssignViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Assignment Koin module. Depends on `accountsModule`/`clientsModule` already being
 * loaded app-wide (for `GetAccountProfilesUseCase`, `GetAccountGroupsUseCase`,
 * `GetClientsUseCase`, `CreateClientUseCase` — Koin resolves across modules).
 */
val assignmentModule = module {
    single<AssignmentRepository> { DefaultAssignmentRepository(get()) }

    factoryOf(::AssignProfileToClientUseCase)
    factoryOf(::GetAvailableProfilesUseCase)
    factoryOf(::GetUpcomingExpirationsUseCase)
    factoryOf(::GetUpcomingExpirationUseCase)
    factoryOf(::RenewAssignmentUseCase)
    factoryOf(::EditAssignmentUseCase)
    factoryOf(::LiberateAssignmentUseCase)

    viewModel { (accountId: String?, profileId: String?) ->
        AssignViewModel(accountId, profileId, get(), get(), get(), get(), get())
    }
}
