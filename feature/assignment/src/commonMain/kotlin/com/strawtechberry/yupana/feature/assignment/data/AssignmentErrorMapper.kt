package com.strawtechberry.yupana.feature.assignment.data

import com.strawtechberry.yupana.feature.assignment.domain.model.AssignmentError

/**
 * Translates supabase-kt/Ktor exceptions into domain [AssignmentError]. Checks for the
 * `assignment` table's unique-index violation first (a race where the profile got
 * assigned between the user seeing it as free and saving), then falls back to the
 * cause-chain network heuristic used across the other error mappers.
 */
internal fun mapAssignmentError(t: Throwable): AssignmentError {
    val text = causeChainText(t)
    return when {
        isUniqueViolation(text) -> AssignmentError.ProfileAlreadyAssigned
        isNetworkError(text) -> AssignmentError.NoConnection
        else -> AssignmentError.Unknown(t.message)
    }
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

/**
 * Postgres/PostgREST unique-violation signature. `23505` is the Postgres error code;
 * the phrase is what a raw `duplicate key value violates unique constraint` message
 * contains. Verified against a real duplicate assignment attempt during manual testing.
 */
private fun isUniqueViolation(text: String): Boolean {
    val markers = listOf("duplicate key", "unique constraint", "23505")
    return markers.any { text.contains(it) }
}

/** Cross-platform network heuristic: looks for typical symptoms in the cause chain. */
private fun isNetworkError(text: String): Boolean {
    val markers = listOf(
        "unknownhost", "unresolved", "connect", "socket", "timeout",
        "network", "failed to connect", "connection", "unreachable", "econn",
    )
    return markers.any { text.contains(it) }
}
