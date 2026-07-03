package com.strawtechberry.yupana.feature.dashboard.di

import com.strawtechberry.yupana.feature.dashboard.ui.allexpirations.AllExpirationsViewModel
import com.strawtechberry.yupana.feature.dashboard.ui.dashboard.DashboardViewModel
import com.strawtechberry.yupana.feature.dashboard.ui.detail.AssignmentDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Dashboard Koin module. No new use cases here — they all live in `assignmentModule`
 * (`:feature:assignment`), already loaded app-wide.
 */
val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AllExpirationsViewModel)

    viewModel { (assignmentId: String) ->
        AssignmentDetailViewModel(assignmentId, get(), get(), get(), get())
    }
}
