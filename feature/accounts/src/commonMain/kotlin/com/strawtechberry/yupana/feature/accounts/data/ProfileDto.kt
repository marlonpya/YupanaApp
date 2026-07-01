package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.model.Profile
import com.strawtechberry.yupana.feature.accounts.domain.model.ProfileOccupant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `profile` table. */
@Serializable
data class ProfileDto(
    val id: String,
    @SerialName("account_id") val accountId: String,
    val label: String,
)

/** Insert body for a blank profile slot created alongside an account. */
@Serializable
data class ProfileInsertDto(
    @SerialName("account_id") val accountId: String,
    val label: String,
)

/** Narrow projection (`id`, `account_id` only) used to compute occupancy counts. */
@Serializable
data class ProfileAccountRefDto(
    val id: String,
    @SerialName("account_id") val accountId: String,
)

fun ProfileDto.toDomain(occupant: ProfileOccupant?): Profile = Profile(
    id = id,
    accountId = accountId,
    label = label,
    occupant = occupant,
)
