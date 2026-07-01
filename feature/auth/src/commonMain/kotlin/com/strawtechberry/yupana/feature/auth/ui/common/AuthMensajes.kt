package com.strawtechberry.yupana.feature.auth.ui.common

import com.strawtechberry.yupana.feature.auth.domain.model.AuthError
import com.strawtechberry.yupana.feature.auth.domain.model.AuthException

/** Traduce un fallo de auth a un mensaje en español para mostrar inline. */
fun mensajeDeError(t: Throwable): String = when ((t as? AuthException)?.error) {
    AuthError.CredencialesInvalidas -> "Correo o contraseña incorrectos"
    AuthError.EmailYaRegistrado -> "Ese correo ya está registrado"
    AuthError.EmailNoConfirmado -> "Confirma tu correo antes de iniciar sesión"
    AuthError.SinConexion -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
    is AuthError.Desconocida, null -> "Algo salió mal. Inténtalo de nuevo"
}
