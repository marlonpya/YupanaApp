package com.strawtechberry.yupana.feature.auth.domain.model

/** Authenticated session of the admin. Minimal user data the app needs. */
data class AuthSession(
    val userId: String,
    val email: String?,
)
