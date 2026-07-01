package com.strawtechberry.yupana.core.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Crea el [SupabaseClient] de Yupana. Instala Auth (flujo PKCE) y Postgrest. El SDK persiste
 * y refresca la sesión automáticamente; el engine Ktor lo aporta cada plataforma
 * (OkHttp en Android, Darwin en iOS) vía la dependencia del sourceSet.
 *
 * @param url URL del proyecto Supabase (nunca hardcodeada; llega desde secrets).
 * @param key publishable key (`sb_publishable_...`), NO la anon key.
 */
fun crearSupabaseClient(url: String, key: String): SupabaseClient =
    createSupabaseClient(supabaseUrl = url, supabaseKey = key) {
        install(Auth) {
            flowType = FlowType.PKCE
        }
        install(Postgrest)
    }
