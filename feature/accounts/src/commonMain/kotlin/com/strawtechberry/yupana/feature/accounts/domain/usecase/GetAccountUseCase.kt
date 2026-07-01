package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Account

/** Loads a single account by id (used by the detail screen and the form's edit mode). */
class GetAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(id: String): Result<Account> = repository.getAccount(id)
}
