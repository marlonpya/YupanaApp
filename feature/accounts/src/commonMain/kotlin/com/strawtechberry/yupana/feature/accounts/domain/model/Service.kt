package com.strawtechberry.yupana.feature.accounts.domain.model

/** A streaming service: global (pre-loaded) or the admin's own. */
data class Service(
    val id: String,
    val name: String,
    val color: String?,
    val logoUrl: String?,
    val isGlobal: Boolean,
)
