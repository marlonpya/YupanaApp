package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository

/** Cierra la sesión del admin. */
class CerrarSesionUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.cerrarSesion()
}
