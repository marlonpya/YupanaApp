package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Account

/** Updates an existing account; grows profile slots if `numProfiles` increased. */
class UpdateAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(
        id: String,
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account> = repository.updateAccount(
        id = id,
        serviceId = serviceId,
        alias = alias.trim(),
        monthlyCost = monthlyCost,
        numProfiles = numProfiles,
        billingDay = billingDay,
        notes = notes?.trim()?.ifBlank { null },
    )
}
