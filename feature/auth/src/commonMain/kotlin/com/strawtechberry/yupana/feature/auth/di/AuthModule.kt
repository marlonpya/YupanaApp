package com.strawtechberry.yupana.feature.auth.di

import com.strawtechberry.yupana.feature.auth.data.DefaultAuthRepository
import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.usecase.CerrarSesionUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.IniciarSesionUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.ObservarEstadoSesionUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.RegistrarUseCase
import com.strawtechberry.yupana.feature.auth.ui.login.LoginViewModel
import com.strawtechberry.yupana.feature.auth.ui.register.RegisterViewModel
import com.strawtechberry.yupana.feature.auth.ui.splash.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Módulo Koin de auth. El `Auth` de supabase lo aporta `supabaseModule` (:core:supabase),
 * así que este módulo debe cargarse junto a aquel.
 */
val authModule = module {
    single<AuthRepository> { DefaultAuthRepository(get()) }

    factoryOf(::IniciarSesionUseCase)
    factoryOf(::RegistrarUseCase)
    factoryOf(::ObservarEstadoSesionUseCase)
    factoryOf(::CerrarSesionUseCase)

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::SplashViewModel)
}
