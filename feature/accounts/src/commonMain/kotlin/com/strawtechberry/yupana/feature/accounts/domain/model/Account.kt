package com.strawtechberry.yupana.feature.accounts.domain.model

/** A concrete streaming account: a specific subscription the admin manages. */
data class Account(
    val id: String,
    val serviceId: String,
    val alias: String,
    val monthlyCost: Double?,
    val numProfiles: Int,
    val billingDay: Int?,
    val notes: String?,
    val createdAt: String?,
)
