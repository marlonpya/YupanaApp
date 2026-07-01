package com.strawtechberry.yupana.feature.auth.domain.model

/** Errores de autenticación en lenguaje de dominio (traducibles a mensajes de UI). */
sealed interface AuthError {
    data object CredencialesInvalidas : AuthError
    data object EmailYaRegistrado : AuthError
    data object EmailNoConfirmado : AuthError
    data object SinConexion : AuthError
    data class Desconocida(val detalle: String?) : AuthError
}

/** Excepción que transporta un [AuthError] a través de `Result.failure`. */
class AuthException(val error: AuthError) : Exception()
