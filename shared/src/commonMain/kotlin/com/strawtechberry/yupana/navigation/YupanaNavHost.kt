package com.strawtechberry.yupana.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.savedstate.read
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.strawtechberry.yupana.feature.accounts.ui.catalog.ServiceCatalogRoute
import com.strawtechberry.yupana.feature.accounts.ui.detail.AccountDetailRoute
import com.strawtechberry.yupana.feature.accounts.ui.form.AccountFormRoute
import com.strawtechberry.yupana.feature.assignment.ui.assign.AssignRoute
import com.strawtechberry.yupana.feature.assignment.ui.move.MoveMemberRoute
import com.strawtechberry.yupana.feature.auth.ui.login.LoginRoute
import com.strawtechberry.yupana.feature.auth.ui.register.RegisterRoute
import com.strawtechberry.yupana.feature.auth.ui.resetpassword.ResetPasswordRoute
import com.strawtechberry.yupana.feature.auth.ui.splash.SplashRoute
import com.strawtechberry.yupana.feature.clients.ui.detail.ClientDetailRoute
import com.strawtechberry.yupana.feature.clients.ui.form.ClientFormRoute
import com.strawtechberry.yupana.feature.dashboard.ui.allexpirations.AllExpirationsRoute
import com.strawtechberry.yupana.feature.dashboard.ui.detail.AssignmentDetailRoute
import com.strawtechberry.yupana.feature.settings.ui.account.MyAccountRoute
import com.strawtechberry.yupana.feature.settings.ui.notifications.NotificationPreferencesRoute
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
                onForgotPassword = { navController.navigate(YupanaDestinations.RESET_PASSWORD) },
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

        composable(YupanaDestinations.RESET_PASSWORD) {
            ResetPasswordRoute(onBack = { navController.popBackStack() })
        }

        composable(YupanaDestinations.DASHBOARD) {
            MainScaffold(
                onCerrarSesion = {
                    navController.navigate(YupanaDestinations.LOGIN) {
                        popUpTo(YupanaDestinations.DASHBOARD) { inclusive = true }
                    }
                },
                onCreateClient = { navController.navigate(YupanaDestinations.CLIENT_FORM_ROUTE) },
                onOpenClientDetail = { id -> navController.navigate("${YupanaDestinations.CLIENT_DETAIL_ROUTE}/$id") },
                onCreateAccount = { navController.navigate(YupanaDestinations.ACCOUNT_FORM_ROUTE) },
                onOpenAccountDetail = { id -> navController.navigate("${YupanaDestinations.ACCOUNT_DETAIL_ROUTE}/$id") },
                onOpenServiceCatalog = { navController.navigate(YupanaDestinations.SERVICE_CATALOG_ROUTE) },
                onCreateAssignment = { navController.navigate(YupanaDestinations.ASSIGN_PROFILE_ROUTE) },
                onOpenAssignmentDetail = { id -> navController.navigate("${YupanaDestinations.ASSIGNMENT_DETAIL_ROUTE}/$id") },
                onOpenAllExpirations = { navController.navigate(YupanaDestinations.ALL_EXPIRATIONS) },
                onOpenNotificationPreferences = { navController.navigate(YupanaDestinations.NOTIFICATION_PREFERENCES) },
                onOpenMyAccount = { navController.navigate(YupanaDestinations.MY_ACCOUNT) },
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

        composable(
            route = YupanaDestinations.CLIENT_DETAIL,
            arguments = listOf(navArgument(YupanaDestinations.CLIENT_DETAIL_ARG_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.read { getString(YupanaDestinations.CLIENT_DETAIL_ARG_ID) }
            if (clientId != null) {
                ClientDetailRoute(
                    clientId = clientId,
                    onBack = { navController.popBackStack() },
                    onEditClient = { id ->
                        navController.navigate("${YupanaDestinations.CLIENT_FORM_ROUTE}?${YupanaDestinations.CLIENT_FORM_ARG_ID}=$id")
                    },
                )
            }
        }

        composable(
            route = YupanaDestinations.ACCOUNT_FORM,
            arguments = listOf(
                navArgument(YupanaDestinations.ACCOUNT_FORM_ARG_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val pickedServiceId by backStackEntry.savedStateHandle
                .getStateFlow<String?>(YupanaDestinations.SELECTED_SERVICE_ID_KEY, null)
                .collectAsStateWithLifecycle()
            AccountFormRoute(
                accountId = backStackEntry.arguments?.read { getStringOrNull(YupanaDestinations.ACCOUNT_FORM_ARG_ID) },
                pickedServiceId = pickedServiceId,
                onServiceConsumed = { backStackEntry.savedStateHandle[YupanaDestinations.SELECTED_SERVICE_ID_KEY] = null },
                onPickService = {
                    navController.navigate("${YupanaDestinations.SERVICE_CATALOG_ROUTE}?${YupanaDestinations.SERVICE_CATALOG_ARG_PICKER}=true")
                },
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = YupanaDestinations.SERVICE_CATALOG,
            arguments = listOf(
                navArgument(YupanaDestinations.SERVICE_CATALOG_ARG_PICKER) {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) { backStackEntry ->
            val picker = backStackEntry.arguments?.read { getBoolean(YupanaDestinations.SERVICE_CATALOG_ARG_PICKER) } ?: false
            ServiceCatalogRoute(
                picker = picker,
                onBack = { navController.popBackStack() },
                onServiceSelected = { id ->
                    if (picker) {
                        navController.previousBackStackEntry?.savedStateHandle?.set(YupanaDestinations.SELECTED_SERVICE_ID_KEY, id)
                        navController.popBackStack()
                    }
                    // En modo standalone no hay pantalla de detalle de servicio en este alcance: no-op.
                },
            )
        }

        composable(
            route = YupanaDestinations.ACCOUNT_DETAIL,
            arguments = listOf(navArgument(YupanaDestinations.ACCOUNT_DETAIL_ARG_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.read { getString(YupanaDestinations.ACCOUNT_DETAIL_ARG_ID) }
            if (accountId != null) {
                AccountDetailRoute(
                    accountId = accountId,
                    onBack = { navController.popBackStack() },
                    onEditAccount = { id ->
                        navController.navigate("${YupanaDestinations.ACCOUNT_FORM_ROUTE}?${YupanaDestinations.ACCOUNT_FORM_ARG_ID}=$id")
                    },
                    onAssignProfile = { accId, profileId ->
                        navController.navigate(
                            "${YupanaDestinations.ASSIGN_PROFILE_ROUTE}?${YupanaDestinations.ASSIGN_ACCOUNT_ARG_ID}=$accId" +
                                "&${YupanaDestinations.ASSIGN_PROFILE_ARG_ID}=$profileId",
                        )
                    },
                )
            }
        }

        composable(
            route = YupanaDestinations.ASSIGN_PROFILE,
            arguments = listOf(
                navArgument(YupanaDestinations.ASSIGN_ACCOUNT_ARG_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(YupanaDestinations.ASSIGN_PROFILE_ARG_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            AssignRoute(
                accountId = backStackEntry.arguments?.read { getStringOrNull(YupanaDestinations.ASSIGN_ACCOUNT_ARG_ID) },
                profileId = backStackEntry.arguments?.read { getStringOrNull(YupanaDestinations.ASSIGN_PROFILE_ARG_ID) },
                onBack = { navController.popBackStack() },
                onAssigned = { navController.popBackStack() },
            )
        }

        composable(
            route = YupanaDestinations.ASSIGNMENT_DETAIL,
            arguments = listOf(navArgument(YupanaDestinations.ASSIGNMENT_DETAIL_ARG_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val assignmentId = backStackEntry.arguments?.read { getString(YupanaDestinations.ASSIGNMENT_DETAIL_ARG_ID) }
            if (assignmentId != null) {
                AssignmentDetailRoute(
                    assignmentId = assignmentId,
                    onBack = { navController.popBackStack() },
                    onActionCompleted = { navController.popBackStack() },
                    onMoveMember = { id -> navController.navigate("${YupanaDestinations.MOVE_MEMBER_ROUTE}/$id") },
                )
            }
        }

        composable(
            route = YupanaDestinations.MOVE_MEMBER,
            arguments = listOf(navArgument(YupanaDestinations.MOVE_MEMBER_ARG_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val assignmentId = backStackEntry.arguments?.read { getString(YupanaDestinations.MOVE_MEMBER_ARG_ID) }
            if (assignmentId != null) {
                MoveMemberRoute(
                    assignmentId = assignmentId,
                    onBack = { navController.popBackStack() },
                    onMoved = { navController.popBackStack(YupanaDestinations.DASHBOARD, inclusive = false) },
                    onCreateAccount = { navController.navigate(YupanaDestinations.ACCOUNT_FORM_ROUTE) },
                )
            }
        }

        composable(YupanaDestinations.ALL_EXPIRATIONS) {
            AllExpirationsRoute(
                onBack = { navController.popBackStack() },
                onOpenDetail = { id -> navController.navigate("${YupanaDestinations.ASSIGNMENT_DETAIL_ROUTE}/$id") },
            )
        }

        composable(YupanaDestinations.NOTIFICATION_PREFERENCES) {
            NotificationPreferencesRoute(onBack = { navController.popBackStack() })
        }

        composable(YupanaDestinations.MY_ACCOUNT) {
            MyAccountRoute(onBack = { navController.popBackStack() })
        }
    }
}
