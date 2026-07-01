package com.strawtechberry.yupana.feature.accounts.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

/**
 * Accounts list route: connects the [AccountsListViewModel] with the UI. Reloads on
 * every re-entry into composition (e.g. after creating/editing an account), same
 * pattern as `ClientsListRoute`.
 */
@Composable
fun AccountsListRoute(
    onCreateAccount: () -> Unit,
    onOpenAccountDetail: (String) -> Unit,
    onOpenServiceCatalog: () -> Unit,
    onCreateAssignment: () -> Unit,
    viewModel: AccountsListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AccountsListEvent.NavigateToCreate -> onCreateAccount()
                AccountsListEvent.NavigateToCatalog -> onOpenServiceCatalog()
                AccountsListEvent.NavigateToNewAssignment -> onCreateAssignment()
                is AccountsListEvent.NavigateToDetail -> onOpenAccountDetail(event.accountId)
            }
        }
    }

    AccountsListScreen(state = state, onIntent = viewModel::onIntent)
}
