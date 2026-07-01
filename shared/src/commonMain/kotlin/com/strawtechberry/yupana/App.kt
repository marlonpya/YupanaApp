package com.strawtechberry.yupana

import androidx.compose.runtime.Composable
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.di.appModules
import com.strawtechberry.yupana.navigation.YupanaNavHost
import org.koin.compose.KoinApplication

/**
 * Punto de entrada COMPARTIDO (Android + iOS). Arranca Koin con los secrets ya cargados por la
 * plataforma ([SupabaseSecrets]) y monta el NavHost bajo el tema oscuro andino.
 */
@Composable
fun App() {
    KoinApplication(application = { modules(appModules(SupabaseSecrets.config)) }) {
        YupanaTheme {
            YupanaNavHost()
        }
    }
}
