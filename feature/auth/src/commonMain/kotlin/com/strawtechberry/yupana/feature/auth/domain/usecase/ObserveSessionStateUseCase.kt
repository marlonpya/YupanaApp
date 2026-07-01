package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.SessionState
import kotlinx.coroutines.flow.Flow

/** Observes the session state; Splash uses it to decide the initial destination. */
class ObserveSessionStateUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<SessionState> = repository.observeSessionState()
}
