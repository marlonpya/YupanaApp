package com.strawtechberry.yupana.feature.auth.domain

import com.strawtechberry.yupana.feature.auth.domain.model.EstadoSesion
import com.strawtechberry.yupana.feature.auth.domain.model.SesionUsuario
import kotlinx.coroutines.flow.Flow

/**
 * Puerto de autenticación. La implementación ([data.DefaultAuthRepository]) usa supabase-kt;
 * el dominio y la presentación no conocen el SDK. Los errores viajan como
 * `Result.failure(AuthException(...))`.
 */
interface AuthRepository {

    /** Inicia sesión con correo y contraseña. */
    suspend fun iniciarSesion(email: String, password: String): Result<Unit>

    /**
     * Registra un admin nuevo. Devuelve la sesión si el proyecto autoconfirma el email
     * (sesión activa inmediata) o `null` si requiere confirmación por correo.
     */
    suspend fun registrar(email: String, password: String): Result<SesionUsuario?>

    /** Sesión actual ya cargada, o `null` si no hay. */
    suspend fun sesionActual(): SesionUsuario?

    /** Estado observable de la sesión (fuente del routing del Splash). */
    fun observarEstadoSesion(): Flow<EstadoSesion>

    /** Cierra la sesión y limpia el almacenamiento seguro. */
    suspend fun cerrarSesion()
}
