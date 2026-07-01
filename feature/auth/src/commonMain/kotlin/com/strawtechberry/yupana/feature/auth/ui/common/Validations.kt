package com.strawtechberry.yupana.feature.auth.ui.common

/** Auth form validations. Return the error message or `null` if the value is valid. */
object Validations {

    private val EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** Supabase Auth's default minimum. */
    const val MIN_PASSWORD = 6

    fun emailError(value: String): String? = when {
        value.isBlank() -> "Ingresa tu correo"
        !EMAIL.matches(value.trim()) -> "Correo no válido"
        else -> null
    }

    fun passwordError(value: String): String? = when {
        value.isBlank() -> "Ingresa tu contraseña"
        value.length < MIN_PASSWORD -> "Mínimo $MIN_PASSWORD caracteres"
        else -> null
    }

    fun confirmPasswordError(password: String, confirmation: String): String? = when {
        confirmation.isBlank() -> "Repite la contraseña"
        confirmation != password -> "Las contraseñas no coinciden"
        else -> null
    }
}
