package com.strawtechberry.yupana.feature.clients.data

import com.strawtechberry.yupana.feature.clients.domain.model.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `client` table. */
@Serializable
data class ClientDto(
    val id: String,
    val name: String,
    val contact: String? = null,
    val notes: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

/**
 * Insert/update body: `owner_id`/`id`/`created_at` are filled or kept server-side,
 * never sent. Same shape works for both operations.
 */
@Serializable
data class ClientUpsertDto(
    val name: String,
    val contact: String? = null,
    val notes: String? = null,
)

fun ClientDto.toDomain(): Client = Client(
    id = id,
    name = name,
    contact = contact,
    notes = notes,
    createdAt = createdAt,
)
