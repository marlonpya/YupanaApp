package com.strawtechberry.yupana.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel base MVI (multiplataforma). Expone un [state] observable y un flujo de [events]
 * de una sola vez. Las subclases reaccionan a las intenciones en [onIntent] y mutan el estado
 * con [setState] o emiten efectos con [sendEvent].
 *
 * @param S estado inmutable de la pantalla.
 * @param I intención (acción del usuario).
 * @param E evento/efecto de una sola vez.
 */
abstract class MviViewModel<S : UiState, I : UiIntent, E : UiEvent>(
    estadoInicial: S,
) : ViewModel() {

    private val _state = MutableStateFlow(estadoInicial)
    val state: StateFlow<S> = _state.asStateFlow()

    // Buffer de eventos: la UI los consume una vez (navegación, snackbars).
    private val _events = Channel<E>(Channel.BUFFERED)
    val events: Flow<E> = _events.receiveAsFlow()

    /** Estado actual sin suscribirse. */
    protected val currentState: S get() = _state.value

    /** Punto único de entrada de las acciones del usuario. */
    abstract fun onIntent(intent: I)

    /** Reduce el estado de forma atómica. */
    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    /** Emite un efecto de una sola vez hacia la UI. */
    protected fun sendEvent(event: E) {
        viewModelScope.launch { _events.send(event) }
    }
}
