package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentMoveContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Assignment row with its profile→account embedded, just to resolve accountId/serviceId. */
@Serializable
data class AssignmentMoveContextDto(
    @SerialName("profile_id") val profileId: String,
    val profile: MoveContextProfileDto? = null,
)

@Serializable
data class MoveContextProfileDto(
    @SerialName("account_id") val accountId: String,
    val account: MoveContextAccountDto? = null,
)

@Serializable
data class MoveContextAccountDto(@SerialName("service_id") val serviceId: String)

fun AssignmentMoveContextDto.toDomain(): AssignmentMoveContext = AssignmentMoveContext(
    profileId = profileId,
    accountId = profile?.accountId ?: "",
    serviceId = profile?.account?.serviceId ?: "",
)

/** Body for "mover a otra cuenta": only the profile changes, price/due date are kept. */
@Serializable
data class AssignmentMoveDto(@SerialName("profile_id") val profileId: String)
