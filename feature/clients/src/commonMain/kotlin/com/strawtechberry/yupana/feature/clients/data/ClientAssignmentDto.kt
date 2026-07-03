package com.strawtechberry.yupana.feature.clients.data

import com.strawtechberry.yupana.feature.clients.domain.model.ClientAssignment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An assignment row for a client, with its profile/account/service context embedded. */
@Serializable
data class ClientAssignmentDto(
    val id: String,
    val status: String,
    @SerialName("price_charged") val priceCharged: Double? = null,
    @SerialName("due_date") val dueDate: String? = null,
    val profile: ClientAssignmentProfileDto? = null,
)

@Serializable
data class ClientAssignmentProfileDto(
    val label: String,
    val account: ClientAssignmentAccountDto? = null,
)

@Serializable
data class ClientAssignmentAccountDto(
    val alias: String,
    @SerialName("streaming_service") val service: ClientAssignmentServiceDto? = null,
)

@Serializable
data class ClientAssignmentServiceDto(val name: String)

fun ClientAssignmentDto.toDomain(): ClientAssignment = ClientAssignment(
    assignmentId = id,
    status = status,
    priceCharged = priceCharged,
    dueDate = dueDate,
    profileLabel = profile?.label ?: "Perfil",
    accountAlias = profile?.account?.alias ?: "",
    serviceName = profile?.account?.service?.name ?: "",
)
