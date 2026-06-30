package com.strawtechberry.yupana

/**
 * Configuración de secrets compartida (Supabase). Fase 1: el mecanismo queda montado
 * con placeholders; el cliente supabase-kt se cablea en Fase 2.
 *
 * Seam de plataforma vía expect/actual: cada plataforma aporta sus valores
 * (Android desde BuildConfig, iOS desde Info.plist/xcconfig). Por ahora ambos
 * devuelven vacío y la app Android los sobreescribe en arranque con [SupabaseSecrets].
 */
data class AppConfig(
    val supabaseUrl: String,
    val supabasePublishableKey: String,
)

/** Valores por defecto provistos por la plataforma (expect/actual). */
expect fun platformConfig(): AppConfig

/** Holder accesible desde commonMain; la app lo inicializa con sus secrets reales. */
object SupabaseSecrets {
    var config: AppConfig = platformConfig()
        private set

    fun override(config: AppConfig) {
        this.config = config
    }
}
