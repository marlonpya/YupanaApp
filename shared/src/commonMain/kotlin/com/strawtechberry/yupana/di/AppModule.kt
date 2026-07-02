package com.strawtechberry.yupana.di

import com.strawtechberry.yupana.AppConfig
import com.strawtechberry.yupana.core.supabase.supabaseModule
import com.strawtechberry.yupana.feature.accounts.di.accountsModule
import com.strawtechberry.yupana.feature.assignment.di.assignmentModule
import com.strawtechberry.yupana.feature.auth.di.authModule
import com.strawtechberry.yupana.feature.clients.di.clientsModule
import com.strawtechberry.yupana.feature.dashboard.di.dashboardModule
import org.koin.core.module.Module

/**
 * Módulos Koin de la app. El de Supabase recibe los secrets (URL + publishable key) que la
 * plataforma cargó en [AppConfig]; el resto de features se apoya en él.
 */
fun appModules(config: AppConfig): List<Module> = listOf(
    supabaseModule(config.supabaseUrl, config.supabasePublishableKey),
    authModule,
    clientsModule,
    accountsModule,
    assignmentModule,
    dashboardModule,
)
