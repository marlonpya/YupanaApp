package com.strawtechberry.yupana.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.savedstate.read
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.strawtechberry.yupana.feature.auth.ui.login.LoginRoute
import com.strawtechberry.yupana.feature.auth.ui.register.RegisterRoute
import com.strawtechberry.yupana.feature.auth.ui.splash.SplashRoute
import com.strawtechberry.yupana.feature.clients.ui.form.ClientFormRoute
import com.strawtechberry.yupana.ui.MainScaffold

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
            MainScaffold(
                onCerrarSesion = {
                    navController.navigate(YupanaDestinations.LOGIN) {
                        popUpTo(YupanaDestinations.DASHBOARD) { inclusive = true }
                    }
                },
                onCreateClient = { navController.navigate(YupanaDestinations.CLIENT_FORM_ROUTE) },
                onEditClient = { id ->
                    navController.navigate("${YupanaDestinations.CLIENT_FORM_ROUTE}?${YupanaDestinations.CLIENT_FORM_ARG_ID}=$id")
                },
            )
        }

        composable(
            route = YupanaDestinations.CLIENT_FORM,
            arguments = listOf(
                navArgument(YupanaDestinations.CLIENT_FORM_ARG_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            ClientFormRoute(
                clientId = backStackEntry.arguments?.read { getStringOrNull(YupanaDestinations.CLIENT_FORM_ARG_ID) },
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
