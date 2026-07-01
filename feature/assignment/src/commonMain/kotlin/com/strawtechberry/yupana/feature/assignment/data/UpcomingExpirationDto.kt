package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.model.UpcomingExpiration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `upcoming_expirations` view. */
@Serializable
data class UpcomingExpirationDto(
    @SerialName("assignment_id") val assignmentId: String,
    @SerialName("due_date") val dueDate: String? = null,
    @SerialName("days_left") val daysLeft: Int? = null,
    @SerialName("price_charged") val priceCharged: Double? = null,
    @SerialName("client_name") val clientName: String,
    @SerialName("client_contact") val clientContact: String? = null,
    @SerialName("profile_label") val profileLabel: String,
    @SerialName("account_alias") val accountAlias: String,
    @SerialName("service_name") val serviceName: String,
)

/** Body for "renovar": pushes `due_date` and resets `reminder_sent`. */
@Serializable
data class AssignmentRenewDto(
    @SerialName("due_date") val dueDate: String,
    @SerialName("reminder_sent") val reminderSent: Boolean = false,
)

/** Body for "editar precio o vencimiento": both fields, always sent together. */
@Serializable
data class AssignmentEditDto(
    @SerialName("price_charged") val priceCharged: Double,
    @SerialName("due_date") val dueDate: String,
)

/** Body for "liberar perfil": soft-cancel. */
@Serializable
data class AssignmentStatusDto(val status: String)

fun UpcomingExpirationDto.toDomain(): UpcomingExpiration = UpcomingExpiration(
    assignmentId = assignmentId,
    dueDate = dueDate,
    daysLeft = daysLeft ?: 0,
    priceCharged = priceCharged,
    clientName = clientName,
    clientContact = clientContact,
    profileLabel = profileLabel,
    accountAlias = accountAlias,
    serviceName = serviceName,
)
