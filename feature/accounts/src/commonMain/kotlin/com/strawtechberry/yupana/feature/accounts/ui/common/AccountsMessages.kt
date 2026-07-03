package com.strawtechberry.yupana.feature.accounts.ui.common

import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsError
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsException

/** Translates an accounts/services operation failure into a Spanish message. */
fun errorMessage(t: Throwable): String = when ((t as? AccountsException)?.error) {
    AccountsError.NoConnection -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
    AccountsError.DuplicateServiceName -> "Ya tienes un servicio con ese nombre"
    AccountsError.ServiceInUse -> "No puedes eliminar este servicio porque tienes cuentas usándolo. " +
        "Elimina o cambia esas cuentas primero."
    is AccountsError.Unknown, null -> "Algo salió mal. Inténtalo de nuevo"
}
