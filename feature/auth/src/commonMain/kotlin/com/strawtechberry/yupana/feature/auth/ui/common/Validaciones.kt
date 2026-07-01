package com.strawtechberry.yupana.feature.auth.ui.common

/** Validaciones de formulario de auth. Devuelven el mensaje de error o `null` si el valor es válido. */
object Validaciones {

    private val EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** Mínimo de Supabase Auth por defecto. */
    const val MIN_PASSWORD = 6

    fun emailError(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu correo"
        !EMAIL.matches(valor.trim()) -> "Correo no válido"
        else -> null
    }

    fun passwordError(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu contraseña"
        valor.length < MIN_PASSWORD -> "Mínimo $MIN_PASSWORD caracteres"
        else -> null
    }

    fun confirmarPasswordError(password: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Repite la contraseña"
        confirmacion != password -> "Las contraseñas no coinciden"
        else -> null
    }
}
