package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Account

/** Creates an account (and its blank profile slots) for the authenticated admin. */
class CreateAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(
        serviceId: String,
        alias: String,
        monthlyCost: Double?,
        numProfiles: Int,
        billingDay: Int?,
        notes: String?,
    ): Result<Account> = repository.createAccount(
        serviceId = serviceId,
        alias = alias.trim(),
        monthlyCost = monthlyCost,
        numProfiles = numProfiles,
        billingDay = billingDay,
        notes = notes?.trim()?.ifBlank { null },
    )
}
