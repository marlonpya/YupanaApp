package com.strawtechberry.yupana.feature.clients.domain.model

/** One of a client's assignments (active or cancelled), with service/account/profile context. */
data class ClientAssignment(
    val assignmentId: String,
    val status: String,
    val priceCharged: Double?,
    val dueDate: String?,
    val profileLabel: String,
    val accountAlias: String,
    val serviceName: String,
)
