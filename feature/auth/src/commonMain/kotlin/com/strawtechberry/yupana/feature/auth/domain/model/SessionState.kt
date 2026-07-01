package com.strawtechberry.yupana.feature.auth.domain.model

/**
 * Session state exposed by the SDK. [Initializing] while it restores from secure
 * storage; then [Authenticated] or [Unauthenticated]. Consumed by Splash to route.
 */
enum class SessionState {
    Initializing,
    Authenticated,
    Unauthenticated,
}
