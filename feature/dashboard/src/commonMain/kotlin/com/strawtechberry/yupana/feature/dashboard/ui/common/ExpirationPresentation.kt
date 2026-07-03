package com.strawtechberry.yupana.feature.dashboard.ui.common

import com.strawtechberry.yupana.core.designsystem.component.YupanaEstado

/** Urgency mapping shared by Dashboard and "Todos los vencimientos". */
fun estadoFor(daysLeft: Int): YupanaEstado = when {
    daysLeft <= 0 -> YupanaEstado.Hoy
    daysLeft <= 7 -> YupanaEstado.Pronto
    else -> YupanaEstado.AlDia
}

fun chipLabelFor(daysLeft: Int): String = when {
    daysLeft < 0 -> "Vencido"
    daysLeft == 0 -> "Hoy"
    daysLeft == 1 -> "Mañana"
    else -> "$daysLeft días"
}
