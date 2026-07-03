package com.strawtechberry.yupana.feature.auth.ui.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Reset password route: connects the [ResetPasswordViewModel] (Koin) with the UI and
 * translates MVI events into navigation. The NavController only lives in the NavHost.
 */
@Composable
fun ResetPasswordRoute(
    onBack: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ResetPasswordEvent.NavigateBack -> onBack()
            }
        }
    }

    ResetPasswordScreen(state = state, onIntent = viewModel::onIntent)
}
