package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.AuthSession

/** Returns the currently signed-in admin's session, or `null` if there isn't one. */
class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AuthSession? = repository.currentSession()
}
