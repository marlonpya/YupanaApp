package com.strawtechberry.yupana.feature.auth.data

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.AuthException
import com.strawtechberry.yupana.feature.auth.domain.model.AuthSession
import com.strawtechberry.yupana.feature.auth.domain.model.SessionState
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [AuthRepository] over supabase-kt. The SDK persists and refreshes
 * the session; this repository only translates between its API and the domain, and
 * maps errors to [AuthException].
 */
class DefaultAuthRepository(private val auth: Auth) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit> =
        execute(AuthOperation.SignIn) {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Unit
        }

    override suspend fun register(email: String, password: String): Result<AuthSession?> =
        execute(AuthOperation.Register) {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            // If the project auto-confirms the email, there's already an active session; if not, it's null.
            auth.currentSessionOrNull()?.toAuthSession()
        }

    override suspend fun currentSession(): AuthSession? =
        auth.currentSessionOrNull()?.toAuthSession()

    override suspend fun resetPassword(email: String): Result<Unit> =
        execute(AuthOperation.ResetPassword) {
            auth.resetPasswordForEmail(email)
            Unit
        }

    override fun observeSessionState(): Flow<SessionState> =
        auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> SessionState.Authenticated
                is SessionStatus.NotAuthenticated -> SessionState.Unauthenticated
                is SessionStatus.RefreshFailure -> SessionState.Unauthenticated
                else -> SessionState.Initializing // transient SDK states
            }
        }

    override suspend fun signOut() {
        auth.signOut()
    }

    /** Executes the operation and wraps the result; propagates cancellation, maps the rest. */
    private suspend fun <T> execute(
        operation: AuthOperation,
        block: suspend () -> T,
    ): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(AuthException(mapAuthError(t, operation)))
    }
}

private fun UserSession.toAuthSession(): AuthSession? =
    user?.let { AuthSession(userId = it.id, email = it.email) }
