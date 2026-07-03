package com.strawtechberry.yupana.feature.auth.domain.model

/** Authentication errors in domain language (translatable to UI messages). */
sealed interface AuthError {
    data object InvalidCredentials : AuthError
    data object EmailAlreadyRegistered : AuthError
    data object EmailNotConfirmed : AuthError
    data object NoConnection : AuthError
    data object InvalidCurrentPassword : AuthError
    data object WeakPassword : AuthError
    data class Unknown(val detail: String?) : AuthError
}

/** Excepción que transporta un [AuthError] a través de `Result.failure`. */
class AuthException(val error: AuthError) : Exception()
