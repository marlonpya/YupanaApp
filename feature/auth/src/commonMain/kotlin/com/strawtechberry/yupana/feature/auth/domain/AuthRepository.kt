package com.strawtechberry.yupana.feature.auth.domain

import com.strawtechberry.yupana.feature.auth.domain.model.AuthSession
import com.strawtechberry.yupana.feature.auth.domain.model.SessionState
import kotlinx.coroutines.flow.Flow

/**
 * Auth port. The implementation ([data.DefaultAuthRepository]) uses supabase-kt;
 * domain and presentation don't know about the SDK. Errors travel as
 * `Result.failure(AuthException(...))`.
 */
interface AuthRepository {

    /** Signs in with email and password. */
    suspend fun signIn(email: String, password: String): Result<Unit>

    /**
     * Registers a new admin. Returns the session if the project auto-confirms the
     * email (session active immediately) or `null` if it requires email confirmation.
     */
    suspend fun register(email: String, password: String): Result<AuthSession?>

    /** Session already loaded, or `null` if there isn't one. */
    suspend fun currentSession(): AuthSession?

    /** Sends a password recovery email. Supabase's hosted page handles setting the new password. */
    suspend fun resetPassword(email: String): Result<Unit>

    /** Observable session state (source of the Splash routing). */
    fun observeSessionState(): Flow<SessionState>

    /** Signs out and clears secure storage. */
    suspend fun signOut()
}
