package com.strawtechberry.yupana.feature.assignment.domain.model

/** A profile linked to a client: price charged and when it's due. */
data class Assignment(
    val id: String,
    val profileId: String,
    val clientId: String,
    val priceCharged: Double?,
    val dueDate: String?,
    val status: String,
)
