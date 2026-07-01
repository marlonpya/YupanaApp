package com.strawtechberry.yupana.feature.clients.ui.common

import com.strawtechberry.yupana.feature.clients.domain.model.ClientError
import com.strawtechberry.yupana.feature.clients.domain.model.ClientException

/** Translates a client operation failure into a Spanish message to display inline. */
fun errorMessage(t: Throwable): String = when ((t as? ClientException)?.error) {
    ClientError.NoConnection -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
    is ClientError.Unknown, null -> "Algo salió mal. Inténtalo de nuevo"
}
