package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.SesionUsuario

/** Registra un admin nuevo. Devuelve la sesión activa o `null` si requiere confirmar el email. */
class RegistrarUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<SesionUsuario?> =
        repository.registrar(email.trim(), password)
}
