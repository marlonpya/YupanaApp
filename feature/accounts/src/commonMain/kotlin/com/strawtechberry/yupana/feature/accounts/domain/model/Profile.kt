package com.strawtechberry.yupana.feature.accounts.domain.model

/** A profile slot within an account: free, or occupied by a client. */
data class Profile(
    val id: String,
    val accountId: String,
    val label: String,
    val occupant: ProfileOccupant?,
) {
    val isFree: Boolean get() = occupant == null
}
