package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Service

/** Creates a custom streaming service owned by the admin. */
class CreateServiceUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(name: String, color: String?): Result<Service> =
        repository.createService(name.trim(), color)
}
