package com.strawtechberry.yupana.feature.auth.domain.model

/**
 * Estado de la sesión que expone el SDK. [Inicializando] mientras se restaura desde el
 * almacenamiento seguro; luego [Autenticada] o [NoAutenticada]. Lo consume el Splash para rutear.
 */
enum class EstadoSesion {
    Inicializando,
    Autenticada,
    NoAutenticada,
}
