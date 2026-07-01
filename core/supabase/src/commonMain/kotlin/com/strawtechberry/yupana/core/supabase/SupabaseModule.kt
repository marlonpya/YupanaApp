package com.strawtechberry.yupana.core.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Módulo Koin del backend. Provee el [SupabaseClient] único y sus plugins ([Auth], [Postgrest])
 * para que los repositorios los inyecten sin conocer supabase-kt directamente.
 *
 * La URL y la key llegan desde los secrets de la app (BuildConfig), nunca hardcodeadas.
 */
fun supabaseModule(url: String, key: String): Module = module {
    single { crearSupabaseClient(url, key) }
    single<Auth> { get<SupabaseClient>().auth }
    single<Postgrest> { get<SupabaseClient>().postgrest }
}
