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

    NavHost(navController = navController, startDestination = YupanaDestinations.SPLASH) {

        composable(YupanaDestinations.SPLASH) {
            SplashRoute(
                onSessionActive = {
                    navController.navigate(YupanaDestinations.DASHBOARD) {
                        popUpTo(YupanaDestinations.SPLASH) { inclusive = true }
                    }
                },
                onNoSession = {
                    navController.navigate(YupanaDestinations.LOGIN) {
                        popUpTo(YupanaDestinations.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(YupanaDestinations.LOGIN) {
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(YupanaDestinations.DASHBOARD) {
                        popUpTo(YupanaDestinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(YupanaDestinations.REGISTER) },
                onForgotPassword = { /* Placeholder: la pantalla real llega en una fase posterior. */ },
            )
        }

        composable(YupanaDestinations.REGISTER) {
            RegisterRoute(
                onRegisterSuccess = {
                    navController.navigate(YupanaDestinations.DASHBOARD) {
                        popUpTo(YupanaDestinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() },
            )
        }

        composable(YupanaDestinations.DASHBOARD) {
            DashboardPlaceholderRoute(
                onCerrarSesion = {
                    navController.navigate(YupanaDestinations.LOGIN) {
                        popUpTo(YupanaDestinations.DASHBOARD) { inclusive = true }
                    }
                },
            )
        }
    }
}
