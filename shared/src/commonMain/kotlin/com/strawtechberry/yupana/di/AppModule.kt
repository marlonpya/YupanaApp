package com.strawtechberry.yupana.di

import com.strawtechberry.yupana.AppConfig
import com.strawtechberry.yupana.core.supabase.supabaseModule
import com.strawtechberry.yupana.feature.auth.di.authModule
import org.koin.core.module.Module

/**
 * Módulos Koin de la app. El de Supabase recibe los secrets (URL + publishable key) que la
 * plataforma cargó en [AppConfig]; el resto de features se apoya en él.
 */
fun appModules(config: AppConfig): List<Module> = listOf(
    supabaseModule(config.supabaseUrl, config.supabasePublishableKey),
    authModule,
)
