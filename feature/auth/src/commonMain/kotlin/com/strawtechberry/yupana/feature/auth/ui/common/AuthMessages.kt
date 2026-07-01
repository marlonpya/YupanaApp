package com.strawtechberry.yupana.feature.auth.ui.common

import com.strawtechberry.yupana.feature.auth.domain.model.AuthError
import com.strawtechberry.yupana.feature.auth.domain.model.AuthException

/** Translates an auth failure into a Spanish message to display inline. */
fun errorMessage(t: Throwable): String = when ((t as? AuthException)?.error) {
    AuthError.InvalidCredentials -> "Correo o contraseña incorrectos"
    AuthError.EmailAlreadyRegistered -> "Ese correo ya está registrado"
    AuthError.EmailNotConfirmed -> "Confirma tu correo antes de iniciar sesión"
    AuthError.NoConnection -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
    is AuthError.Unknown, null -> "Algo salió mal. Inténtalo de nuevo"
}
