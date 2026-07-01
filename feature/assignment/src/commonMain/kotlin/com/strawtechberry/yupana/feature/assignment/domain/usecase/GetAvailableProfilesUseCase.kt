package com.strawtechberry.yupana.feature.assignment.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import com.strawtechberry.yupana.feature.accounts.domain.usecase.GetAccountProfilesUseCase

/**
 * Free profiles of an account, ready to be assigned. Wraps [GetAccountProfilesUseCase]
 * (from `:feature:accounts`, Grupo 3) instead of querying again — the account/profile
 * query already exists there.
 */
class GetAvailableProfilesUseCase(private val getAccountProfiles: GetAccountProfilesUseCase) {
    suspend operator fun invoke(accountId: String): Result<List<Profile>> =
        getAccountProfiles(accountId).map { profiles -> profiles.filter { it.isFree } }
}
