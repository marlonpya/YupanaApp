package com.strawtechberry.yupana.feature.auth.data

import com.strawtechberry.yupana.feature.auth.domain.model.AuthError

/** Operation context to disambiguate generic errors (a 400 on login = credentials). */
internal enum class AuthOperation { SignIn, Register, ResetPassword }

/**
 * Translates supabase-kt/Ktor exceptions into domain [AuthError]. Relies on the error
 * text and the cause chain (without depending on platform types) to work across KMP.
 */
internal fun mapAuthError(t: Throwable, operation: AuthOperation): AuthError {
    val text = causeChainText(t)

    return when {
        isNetworkError(t, text) -> AuthError.NoConnection

        text.contains("email not confirmed") ||
            text.contains("email_not_confirmed") -> AuthError.EmailNotConfirmed

        text.contains("already registered") ||
            text.contains("already exists") ||
            text.contains("user_already_exists") ||
            text.contains("email_exists") -> AuthError.EmailAlreadyRegistered

        text.contains("invalid login") ||
            text.contains("invalid_credentials") ||
            text.contains("invalid credentials") -> AuthError.InvalidCredentials

        // A failure without a clear message on sign in is usually invalid credentials.
        operation == AuthOperation.SignIn && text.contains("400") -> AuthError.InvalidCredentials

        else -> AuthError.Unknown(t.message)
    }
}

/** Joins message and class names of the whole cause chain, lowercased. */
private fun causeChainText(t: Throwable): String {
    val sb = StringBuilder()
    var current: Throwable? = t
    var depth = 0
    while (current != null && depth < 10) {
        sb.append(current::class.simpleName).append(' ').append(current.message).append(' ')
        current = current.cause
        depth++
    }
    return sb.toString().lowercase()
}

/** Cross-platform network heuristic: looks for typical symptoms in the cause chain. */
private fun isNetworkError(t: Throwable, text: String): Boolean {
    val markers = listOf(
        "unknownhost", "unresolved", "connect", "socket", "timeout",
        "network", "failed to connect", "connection", "unreachable", "econn",
    )
    return markers.any { text.contains(it) }
}
