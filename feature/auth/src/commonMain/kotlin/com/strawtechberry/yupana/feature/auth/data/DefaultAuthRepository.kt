package com.strawtechberry.yupana.feature.auth.data

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.AuthException
import com.strawtechberry.yupana.feature.auth.domain.model.EstadoSesion
import com.strawtechberry.yupana.feature.auth.domain.model.SesionUsuario
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación de [AuthRepository] sobre supabase-kt. El SDK persiste y refresca la sesión;
 * este repositorio solo traduce entre su API y el dominio, y mapea errores a [AuthException].
 */
class DefaultAuthRepository(private val auth: Auth) : AuthRepository {

    override suspend fun iniciarSesion(email: String, password: String): Result<Unit> =
        ejecutar(OperacionAuth.IniciarSesion) {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Unit
        }

    override suspend fun registrar(email: String, password: String): Result<SesionUsuario?> =
        ejecutar(OperacionAuth.Registrar) {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            // Si el proyecto autoconfirma el email, ya hay sesión activa; si no, es null.
            auth.currentSessionOrNull()?.toSesionUsuario()
        }

    override suspend fun sesionActual(): SesionUsuario? =
        auth.currentSessionOrNull()?.toSesionUsuario()

    override fun observarEstadoSesion(): Flow<EstadoSesion> =
        auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> EstadoSesion.Autenticada
                is SessionStatus.NotAuthenticated -> EstadoSesion.NoAutenticada
                is SessionStatus.RefreshFailure -> EstadoSesion.NoAutenticada
                else -> EstadoSesion.Inicializando // Initializing / estados transitorios del SDK
            }
        }

    override suspend fun cerrarSesion() {
        auth.signOut()
    }

    /** Ejecuta la operación y envuelve el resultado; propaga cancelación, mapea el resto. */
    private suspend fun <T> ejecutar(
        operacion: OperacionAuth,
        bloque: suspend () -> T,
    ): Result<T> = try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AuthException(mapearAuthError(t, operacion)))
    }
}

private fun UserSession.toSesionUsuario(): SesionUsuario? =
    user?.let { SesionUsuario(userId = it.id, email = it.email) }
