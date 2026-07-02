package com.strawtechberry.yupana.feature.assignment.domain.model

/** A row of the `upcoming_expirations` view: an active assignment with its urgency. */
data class UpcomingExpiration(
    val assignmentId: String,
    val dueDate: String?,
    val daysLeft: Int,
    val priceCharged: Double?,
    val clientName: String,
    val clientContact: String?,
    val profileLabel: String,
    val accountAlias: String,
    val serviceName: String,
)
