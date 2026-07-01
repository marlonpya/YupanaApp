package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Service

/** Lists global + the admin's own streaming services, ordered by name. */
class GetServicesUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(): Result<List<Service>> = repository.getServices()
}
