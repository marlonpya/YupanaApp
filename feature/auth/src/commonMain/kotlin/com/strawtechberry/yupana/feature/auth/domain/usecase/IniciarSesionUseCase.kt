package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository

/** Inicia sesión con correo y contraseña ya validados por la capa de presentación. */
class IniciarSesionUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.iniciarSesion(email.trim(), password)
}
