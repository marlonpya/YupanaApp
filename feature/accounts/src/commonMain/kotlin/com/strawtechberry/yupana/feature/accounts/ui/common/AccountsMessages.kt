package com.strawtechberry.yupana.feature.accounts.ui.common

import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsError
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsException

/** Translates an accounts/services operation failure into a Spanish message. */
fun errorMessage(t: Throwable): String = when ((t as? AccountsException)?.error) {
    AccountsError.NoConnection -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
    is AccountsError.Unknown, null -> "Algo salió mal. Inténtalo de nuevo"
}
