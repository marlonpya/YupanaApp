package com.strawtechberry.yupana.feature.auth.domain.model

/** Sesión autenticada del admin. Datos mínimos que la app necesita del usuario. */
data class SesionUsuario(
    val userId: String,
    val email: String?,
)
