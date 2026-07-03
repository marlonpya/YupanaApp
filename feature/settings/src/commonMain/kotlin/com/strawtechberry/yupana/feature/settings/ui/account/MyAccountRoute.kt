package com.strawtechberry.yupana.feature.settings.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyAccountRoute(
    onBack: () -> Unit,
    viewModel: MyAccountViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MyAccountEvent.NavigateBack -> onBack()
            }
        }
    }

    MyAccountScreen(state = state, onIntent = viewModel::onIntent)
}
