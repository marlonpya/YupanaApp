package com.strawtechberry.yupana.feature.clients.domain.model

/** Client operation errors in domain language (translatable to UI messages). */
sealed interface ClientError {
    data object NoConnection : ClientError
    data class Unknown(val detail: String?) : ClientError
}

/** Exception that carries a [ClientError] through `Result.failure`. */
class ClientException(val error: ClientError) : Exception()
