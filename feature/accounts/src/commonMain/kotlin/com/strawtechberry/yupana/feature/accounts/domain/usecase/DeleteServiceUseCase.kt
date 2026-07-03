package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository

/** Deletes one of the admin's own custom services (fails if an account still uses it). */
class DeleteServiceUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteService(id)
}
