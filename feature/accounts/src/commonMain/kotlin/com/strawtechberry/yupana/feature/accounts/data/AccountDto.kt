package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.model.Account
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `account` table. */
@Serializable
data class AccountDto(
    val id: String,
    @SerialName("service_id") val serviceId: String,
    val alias: String,
    @SerialName("monthly_cost") val monthlyCost: Double? = null,
    @SerialName("num_profiles") val numProfiles: Int = 1,
    @SerialName("billing_day") val billingDay: Int? = null,
    val notes: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

/**
 * Insert/update body for an account. Same shape works for both operations, mirroring
 * `ClientUpsertDto` from `:feature:clients`. `owner_id`/`id`/`created_at` are filled
 * or kept server-side, never sent.
 */
@Serializable
data class AccountUpsertDto(
    @SerialName("service_id") val serviceId: String,
    val alias: String,
    @SerialName("monthly_cost") val monthlyCost: Double? = null,
    @SerialName("num_profiles") val numProfiles: Int,
    @SerialName("billing_day") val billingDay: Int? = null,
    val notes: String? = null,
)

fun AccountDto.toDomain(): Account = Account(
    id = id,
    serviceId = serviceId,
    alias = alias,
    monthlyCost = monthlyCost,
    numProfiles = numProfiles,
    billingDay = billingDay,
    notes = notes,
    createdAt = createdAt,
)
