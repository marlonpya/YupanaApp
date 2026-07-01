package com.strawtechberry.yupana.feature.accounts.data

import com.strawtechberry.yupana.feature.accounts.domain.model.AccountsError

/**
 * Translates supabase-kt/Ktor exceptions into domain [AccountsError]. Relies on the
 * cause chain (without depending on platform types) to work across KMP.
 */
internal fun mapAccountsError(t: Throwable): AccountsError {
    val text = causeChainText(t)
    return if (isNetworkError(text)) AccountsError.NoConnection else AccountsError.Unknown(t.message)
}

/** Joins message and class names of the whole cause chain, lowercased. */
private fun causeChainText(t: Throwable): String {
    val sb = StringBuilder()
    var current: Throwable? = t
    var depth = 0
    while (current != null && depth < 10) {
        sb.append(current::class.simpleName).append(' ').append(current.message).append(' ')
        current = current.cause
        depth++
    }
    return sb.toString().lowercase()
}

/** Cross-platform network heuristic: looks for typical symptoms in the cause chain. */
private fun isNetworkError(text: String): Boolean {
    val markers = listOf(
        "unknownhost", "unresolved", "connect", "socket", "timeout",
        "network", "failed to connect", "connection", "unreachable", "econn",
    )
    return markers.any { text.contains(it) }
}
