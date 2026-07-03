package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Service

/** Updates one of the admin's own custom services. */
class UpdateServiceUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(id: String, name: String, color: String?): Result<Service> =
        repository.updateService(id, name.trim(), color)
}
