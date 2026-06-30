package com.strawtechberry.yupana

// iOS: en fase posterior se leerá de Info.plist / xcconfig. Fase 1: placeholder.
actual fun platformConfig(): AppConfig = AppConfig(
    supabaseUrl = "",
    supabasePublishableKey = "",
)
