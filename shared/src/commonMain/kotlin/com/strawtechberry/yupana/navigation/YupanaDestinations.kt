package com.strawtechberry.yupana.navigation

/** Rutas del NavHost central. */
object YupanaDestinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard"
    const val CLIENT_FORM_ARG_ID = "clientId"
    const val CLIENT_FORM_ROUTE = "client_form"
    const val CLIENT_FORM = "$CLIENT_FORM_ROUTE?$CLIENT_FORM_ARG_ID={$CLIENT_FORM_ARG_ID}"
}
