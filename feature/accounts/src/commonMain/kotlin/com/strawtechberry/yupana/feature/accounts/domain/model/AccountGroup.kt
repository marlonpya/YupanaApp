package com.strawtechberry.yupana.feature.accounts.domain.model

/** Accounts of the same [Service], grouped for the "Mis cuentas" list. */
data class AccountGroup(
    val service: Service,
    val accounts: List<AccountWithOccupancy>,
) {
    val totalOccupied: Int get() = accounts.sumOf { it.occupiedProfiles }
    val totalProfiles: Int get() = accounts.sumOf { it.totalProfiles }
}
