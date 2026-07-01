package com.strawtechberry.yupana.feature.accounts.domain.model

/** The client currently occupying a profile, if any. */
data class ProfileOccupant(
    val clientName: String,
    val priceCharged: Double?,
    val dueDate: String?,
)
