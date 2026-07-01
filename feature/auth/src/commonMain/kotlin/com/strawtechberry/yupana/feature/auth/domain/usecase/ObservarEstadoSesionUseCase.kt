package com.strawtechberry.yupana.feature.auth.domain.usecase

import com.strawtechberry.yupana.feature.auth.domain.AuthRepository
import com.strawtechberry.yupana.feature.auth.domain.model.EstadoSesion
import kotlinx.coroutines.flow.Flow

/** Observa el estado de la sesión; el Splash lo usa para decidir el destino inicial. */
class ObservarEstadoSesionUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<EstadoSesion> = repository.observarEstadoSesion()
}
