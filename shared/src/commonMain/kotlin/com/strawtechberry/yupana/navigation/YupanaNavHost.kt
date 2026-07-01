package com.strawtechberry.yupana.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.strawtechberry.yupana.feature.auth.ui.login.LoginRoute
import com.strawtechberry.yupana.feature.auth.ui.register.RegisterRoute
import com.strawtechberry.yupana.feature.auth.ui.splash.SplashRoute
import com.strawtechberry.yupana.ui.DashboardPlaceholderRoute

/**
 * NavHost central de Yupana. El NavController vive solo aquí; cada *Route recibe callbacks de
 * navegación y no conoce el grafo. `popUpTo` evita volver al Splash/Login con el botón atrás.
 */
@Composable
fun YupanaNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = YupanaDestinos.SPLASH) {

        composable(YupanaDestinos.SPLASH) {
            SplashRoute(
                onSesionActiva = {
                    navController.navigate(YupanaDestinos.DASHBOARD) {
                        popUpTo(YupanaDestinos.SPLASH) { inclusive = true }
                    }
                },
                onSinSesion = {
                    navController.navigate(YupanaDestinos.LOGIN) {
                        popUpTo(YupanaDestinos.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(YupanaDestinos.LOGIN) {
            LoginRoute(
                onLoginExitoso = {
                    navController.navigate(YupanaDestinos.DASHBOARD) {
                        popUpTo(YupanaDestinos.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(YupanaDestinos.REGISTER) },
                onOlvidePassword = { /* Placeholder: la pantalla real llega en una fase posterior. */ },
            )
        }

        composable(YupanaDestinos.REGISTER) {
            RegisterRoute(
                onRegistroExitoso = {
                    navController.navigate(YupanaDestinos.DASHBOARD) {
                        popUpTo(YupanaDestinos.LOGIN) { inclusive = true }
                    }
                },
                onIrALogin = { navController.popBackStack() },
            )
        }

        composable(YupanaDestinos.DASHBOARD) {
            DashboardPlaceholderRoute(
                onCerrarSesion = {
                    navController.navigate(YupanaDestinos.LOGIN) {
                        popUpTo(YupanaDestinos.DASHBOARD) { inclusive = true }
                    }
                },
            )
        }
    }
}
