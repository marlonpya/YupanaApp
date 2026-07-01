package com.strawtechberry.yupana.feature.clients.domain.model

/** A reseller's client. Minimal data needed to assign profiles and reach them. */
data class Client(
    val id: String,
    val name: String,
    val contact: String?,
    val notes: String?,
    val createdAt: String?,
)
