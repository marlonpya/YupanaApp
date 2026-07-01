package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.model.Assignment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `assignment` table. */
@Serializable
data class AssignmentDto(
    val id: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("price_charged") val priceCharged: Double? = null,
    @SerialName("due_date") val dueDate: String? = null,
    val status: String = "active",
)

/**
 * Insert body. `owner_id`/`start_date`/`status` all have database defaults, so they're
 * never sent.
 */
@Serializable
data class AssignmentInsertDto(
    @SerialName("profile_id") val profileId: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("price_charged") val priceCharged: Double,
    @SerialName("due_date") val dueDate: String,
)

fun AssignmentDto.toDomain(): Assignment = Assignment(
    id = id,
    profileId = profileId,
    clientId = clientId,
    priceCharged = priceCharged,
    dueDate = dueDate,
    status = status,
)
