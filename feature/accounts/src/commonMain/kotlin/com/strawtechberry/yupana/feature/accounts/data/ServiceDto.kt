package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.model.Service
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Mirrors a row of the `streaming_service` table. */
@Serializable
data class ServiceDto(
    val id: String,
    val name: String,
    val color: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("is_global") val isGlobal: Boolean = false,
)

/**
 * Insert body for a custom service. `streaming_service.owner_id` has NO database
 * default (unlike account/profile), so it must be sent explicitly.
 */
@Serializable
data class ServiceInsertDto(
    val name: String,
    val color: String? = null,
    @SerialName("owner_id") val ownerId: String,
)

/** Update body for one of the admin's own custom services (`id` goes in the filter, not the body). */
@Serializable
data class ServiceUpdateDto(
    val name: String,
    val color: String? = null,
)

fun ServiceDto.toDomain(): Service = Service(
    id = id,
    name = name,
    color = color,
    logoUrl = logoUrl,
    isGlobal = isGlobal,
)
