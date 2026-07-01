package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile

/** Lists the profiles of one account, each with its occupant if assigned. */
class GetAccountProfilesUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(accountId: String): Result<List<Profile>> =
        repository.getProfilesForAccount(accountId)
}
