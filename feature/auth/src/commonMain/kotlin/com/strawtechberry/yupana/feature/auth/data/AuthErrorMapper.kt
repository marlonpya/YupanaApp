package com.strawtechberry.yupana.feature.auth.data

import com.strawtechberry.yupana.feature.auth.domain.model.AuthError

/** Contexto de la operación para desambiguar errores genéricos (un 400 en login = credenciales). */
internal enum class OperacionAuth { IniciarSesion, Registrar }

/**
 * Traduce las excepciones de supabase-kt/Ktor a [AuthError] de dominio. Se apoya en el texto del
 * error y en la cadena de causas (sin depender de tipos de plataforma) para funcionar en KMP.
 */
internal fun mapearAuthError(t: Throwable, operacion: OperacionAuth): AuthError {
    val texto = cadenaDeTexto(t)

    return when {
        esErrorDeRed(t, texto) -> AuthError.SinConexion

        texto.contains("email not confirmed") ||
            texto.contains("email_not_confirmed") -> AuthError.EmailNoConfirmado

        texto.contains("already registered") ||
            texto.contains("already exists") ||
            texto.contains("user_already_exists") ||
            texto.contains("email_exists") -> AuthError.EmailYaRegistrado

        texto.contains("invalid login") ||
            texto.contains("invalid_credentials") ||
            texto.contains("invalid credentials") -> AuthError.CredencialesInvalidas

        // Un fallo sin mensaje claro al iniciar sesión suele ser credenciales inválidas.
        operacion == OperacionAuth.IniciarSesion && texto.contains("400") -> AuthError.CredencialesInvalidas

        else -> AuthError.Desconocida(t.message)
    }
}

/** Une mensaje y nombres de clase de toda la cadena de causas, en minúsculas. */
private fun cadenaDeTexto(t: Throwable): String {
    val sb = StringBuilder()
    var actual: Throwable? = t
    var guarda = 0
    while (actual != null && guarda < 10) {
        sb.append(actual::class.simpleName).append(' ').append(actual.message).append(' ')
        actual = actual.cause
        guarda++
    }
    return sb.toString().lowercase()
}

/** Heurística de red multiplataforma: busca síntomas típicos en la cadena de causas. */
private fun esErrorDeRed(t: Throwable, texto: String): Boolean {
    val marcadores = listOf(
        "unknownhost", "unresolved", "connect", "socket", "timeout",
        "network", "failed to connect", "connection", "unreachable", "econn",
    )
    return marcadores.any { texto.contains(it) }
}
