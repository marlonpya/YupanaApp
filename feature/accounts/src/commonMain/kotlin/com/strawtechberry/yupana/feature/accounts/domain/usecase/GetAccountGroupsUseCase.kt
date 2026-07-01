package com.strawtechberry.yupana.feature.accounts.domain.usecase

import com.strawtechberry.yupana.feature.accounts.domain.AccountRepository
import com.strawtechberry.yupana.feature.accounts.domain.ServiceRepository
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountWithOccupancy

/**
 * Loads all accounts grouped by service, with occupancy counts, filtered by [query]
 * against the alias or the service name (case-insensitive substring, client-side —
 * grouping/filtering is presentation logic, not a database view).
 */
class GetAccountGroupsUseCase(
    private val accountRepository: AccountRepository,
    private val serviceRepository: ServiceRepository,
) {
    suspend operator fun invoke(query: String): Result<List<AccountGroup>> {
        val services = serviceRepository.getServices().fold({ it }, { return Result.failure(it) })
        val accounts = accountRepository.getAccounts().fold({ it }, { return Result.failure(it) })
        val occupiedCounts = accountRepository.getOccupiedProfileCounts().fold({ it }, { return Result.failure(it) })

        val servicesById = services.associateBy { it.id }
        val matches = { text: String -> query.isBlank() || text.contains(query, ignoreCase = true) }

        val groups = accounts
            .filter { account ->
                val serviceName = servicesById[account.serviceId]?.name.orEmpty()
                matches(account.alias) || matches(serviceName)
            }
            .groupBy { it.serviceId }
            .mapNotNull { (serviceId, accountsForService) ->
                val service = servicesById[serviceId] ?: return@mapNotNull null
                AccountGroup(
                    service = service,
                    accounts = accountsForService.map { account ->
                        AccountWithOccupancy(account, occupiedCounts[account.id] ?: 0)
                    },
                )
            }
            .sortedBy { it.service.name }

        return Result.success(groups)
    }
}
