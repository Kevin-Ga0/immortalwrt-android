package com.immortalwrt.manager.core.session

enum class SessionState {
    UNAUTHENTICATED,
    AUTHENTICATING,
    AUTHENTICATED,
    EXPIRING_SOON,
    REFRESHING,
    EXPIRED
}
