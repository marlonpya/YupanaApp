package com.strawtechberry.yupana

// Android: la app sobreescribe estos valores en arranque con BuildConfig (secrets.properties).
actual fun platformConfig(): AppConfig = AppConfig(
    supabaseUrl = "",
    supabasePublishableKey = "",
)
