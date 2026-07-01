package com.strawtechberry.yupana.feature.accounts.domain.model

/** An [Account] paired with how many of its profile slots are currently occupied. */
data class AccountWithOccupancy(
    val account: Account,
    val occupiedProfiles: Int,
) {
    val totalProfiles: Int get() = account.numProfiles
    val occupancyRatio: Float get() = if (totalProfiles == 0) 0f else occupiedProfiles.toFloat() / totalProfiles
}
