package com.strawtechberry.yupana.feature.accounts.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Account form route: create/edit, mode decided by whether [accountId] is null.
 * [pickedServiceId]/[onServiceConsumed] plumb the result of the service-picker
 * navigation flow — the picker's `SavedStateHandle` handling lives entirely in
 * `YupanaNavHost`, so this Route stays free of `androidx.navigation` imports like
 * every other Route in the app.
 */
@Composable
fun AccountFormRoute(
    accountId: String?,
    pickedServiceId: String?,
    onServiceConsumed: () -> Unit,
    onPickService: () -> Unit,
    onSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: AccountFormViewModel = koinViewModel(key = accountId) { parametersOf(accountId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(pickedServiceId) {
        if (pickedServiceId != null) {
            viewModel.onIntent(AccountFormIntent.ServiceSelected(pickedServiceId))
            onServiceConsumed()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AccountFormEvent.NavigateBack -> onBack()
                AccountFormEvent.NavigateToServicePicker -> onPickService()
                AccountFormEvent.SavedSuccessfully -> onSaved()
            }
        }
    }

    AccountFormScreen(state = state, onIntent = viewModel::onIntent)
}
