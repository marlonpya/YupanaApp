package com.strawtechberry.yupana.core.mvi

/**
 * Contrato MVI de Yupana. Cada pantalla define su propio [UiState] (data class inmutable),
 * su [UiIntent] (acciones del usuario) y su [UiEvent] (efectos de una sola vez: navegar,
 * mostrar mensaje). Marcadores vacíos para atar los genéricos de [MviViewModel].
 */
interface UiState

interface UiIntent

interface UiEvent
