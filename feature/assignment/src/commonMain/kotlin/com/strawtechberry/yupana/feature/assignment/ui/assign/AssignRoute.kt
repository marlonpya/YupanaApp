package com.strawtechberry.yupana.feature.assignment.ui.assign

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Assign route. [accountId]/[profileId] come pre-filled from the account detail
 * screen's free profile (entry A), or both `null` for the "nueva asignación desde
 * cero" entry point (entry B).
 */
@Composable
fun AssignRoute(
    accountId: String?,
    profileId: String?,
    onBack: () -> Unit,
    onAssigned: () -> Unit,
    viewModel: AssignViewModel = koinViewModel { parametersOf(accountId, profileId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AssignEvent.NavigateBack -> onBack()
                AssignEvent.AssignedSuccessfully -> onAssigned()
            }
        }
    }

    AssignScreen(state = state, onIntent = viewModel::onIntent)
}
