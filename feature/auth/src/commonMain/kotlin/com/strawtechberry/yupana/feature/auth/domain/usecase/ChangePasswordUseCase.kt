package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository

/** Changes the signed-in admin's password, re-verifying the current one server-side. */
class ChangePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> =
        repository.changePassword(currentPassword, newPassword)
}
