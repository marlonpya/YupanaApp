package com.strawtechberry.yupana.feature.assignment.ui.common

import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsException
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentError
import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentException
import com.strawtechberry.yupana.feature.clients.domain.model.ClientException
import com.strawtechberry.yupana.feature.accounts.ui.common.errorMessage as accountsErrorMessage
import com.strawtechberry.yupana.feature.clients.ui.common.errorMessage as clientsErrorMessage

/**
 * Translates a failure from any of the three domains this screen composes
 * (assignment/accounts/clients) into a Spanish message to display inline.
 */
fun errorMessage(t: Throwable): String = when (t) {
    is AssignmentException -> when (t.error) {
        AssignmentError.NoConnection -> "Sin conexión. Revisa tu internet e inténtalo de nuevo"
        AssignmentError.ProfileAlreadyAssigned -> "Este perfil ya no está disponible"
        is AssignmentError.Unknown -> "Algo salió mal. Inténtalo de nuevo"
    }
    is AccountsException -> accountsErrorMessage(t)
    is ClientException -> clientsErrorMessage(t)
    else -> "Algo salió mal. Inténtalo de nuevo"
}
