package com.strawtechberry.yupana.feature.auth.di

import com.strawtechberry.yupana.feature.auth.data.DefaultAuthRepository
import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.usecase.ObserveSessionStateUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.RegisterUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.ResetPasswordUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.SignInUseCase
import com.strawtechberry.yupana.feature.auth.domain.usecase.SignOutUseCase
import com.strawtechberry.yupana.feature.auth.ui.login.LoginViewModel
import com.strawtechberry.yupana.feature.auth.ui.register.RegisterViewModel
import com.strawtechberry.yupana.feature.auth.ui.resetpassword.ResetPasswordViewModel
import com.strawtechberry.yupana.feature.auth.ui.splash.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Auth Koin module. supabase's `Auth` is provided by `supabaseModule` (:core:supabase),
 * so this module must be loaded alongside it.
 */
val authModule = module {
    single<AuthRepository> { DefaultAuthRepository(get()) }

    factoryOf(::SignInUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::ObserveSessionStateUseCase)
    factoryOf(::SignOutUseCase)
    factoryOf(::ResetPasswordUseCase)

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::ResetPasswordViewModel)
}
