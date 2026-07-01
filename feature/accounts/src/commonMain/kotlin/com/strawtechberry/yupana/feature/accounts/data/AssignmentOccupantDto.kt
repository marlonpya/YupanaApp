package com.strawtechberry.yupana.feature.accounts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An active assignment with its client's name embedded, for the account detail screen. */
@Serializable
data class AssignmentOccupantDto(
    @SerialName("profile_id") val profileId: String,
    @SerialName("price_charged") val priceCharged: Double? = null,
    @SerialName("due_date") val dueDate: String? = null,
    val client: AssignmentClientDto? = null,
)

@Serializable
data class AssignmentClientDto(val name: String)

/** Narrow projection used only to know which profile ids are currently occupied. */
@Serializable
data class AssignmentProfileIdDto(
    @SerialName("profile_id") val profileId: String,
)
