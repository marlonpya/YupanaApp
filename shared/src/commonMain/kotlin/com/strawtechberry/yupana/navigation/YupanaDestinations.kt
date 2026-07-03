package com.strawtechberry.yupana.navigation

/** Rutas del NavHost central. */
object YupanaDestinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val RESET_PASSWORD = "reset_password"
    const val DASHBOARD = "dashboard"
    const val CLIENT_FORM_ARG_ID = "clientId"
    const val CLIENT_FORM_ROUTE = "client_form"
    const val CLIENT_FORM = "$CLIENT_FORM_ROUTE?$CLIENT_FORM_ARG_ID={$CLIENT_FORM_ARG_ID}"

    const val CLIENT_DETAIL_ARG_ID = "clientId"
    const val CLIENT_DETAIL_ROUTE = "client_detail"
    const val CLIENT_DETAIL = "$CLIENT_DETAIL_ROUTE/{$CLIENT_DETAIL_ARG_ID}"

    const val ACCOUNT_DETAIL_ARG_ID = "accountId"
    const val ACCOUNT_DETAIL_ROUTE = "account_detail"
    const val ACCOUNT_DETAIL = "$ACCOUNT_DETAIL_ROUTE/{$ACCOUNT_DETAIL_ARG_ID}"

    const val ACCOUNT_FORM_ARG_ID = "accountId"
    const val ACCOUNT_FORM_ROUTE = "account_form"
    const val ACCOUNT_FORM = "$ACCOUNT_FORM_ROUTE?$ACCOUNT_FORM_ARG_ID={$ACCOUNT_FORM_ARG_ID}"

    const val SERVICE_CATALOG_ARG_PICKER = "picker"
    const val SERVICE_CATALOG_ROUTE = "service_catalog"
    const val SERVICE_CATALOG = "$SERVICE_CATALOG_ROUTE?$SERVICE_CATALOG_ARG_PICKER={$SERVICE_CATALOG_ARG_PICKER}"

    /** SavedStateHandle key used to pass the picked service id back to the account form. */
    const val SELECTED_SERVICE_ID_KEY = "selected_service_id"

    const val ASSIGN_ACCOUNT_ARG_ID = "accountId"
    const val ASSIGN_PROFILE_ARG_ID = "profileId"
    const val ASSIGN_PROFILE_ROUTE = "assign_profile"
    const val ASSIGN_PROFILE =
        "$ASSIGN_PROFILE_ROUTE?$ASSIGN_ACCOUNT_ARG_ID={$ASSIGN_ACCOUNT_ARG_ID}&$ASSIGN_PROFILE_ARG_ID={$ASSIGN_PROFILE_ARG_ID}"

    const val ASSIGNMENT_DETAIL_ARG_ID = "assignmentId"
    const val ASSIGNMENT_DETAIL_ROUTE = "assignment_detail"
    const val ASSIGNMENT_DETAIL = "$ASSIGNMENT_DETAIL_ROUTE/{$ASSIGNMENT_DETAIL_ARG_ID}"

    const val MOVE_MEMBER_ARG_ID = "assignmentId"
    const val MOVE_MEMBER_ROUTE = "move_member"
    const val MOVE_MEMBER = "$MOVE_MEMBER_ROUTE/{$MOVE_MEMBER_ARG_ID}"

    const val ALL_EXPIRATIONS = "all_expirations"
}
