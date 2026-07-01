package com.strawtechberry.yupana.feature.accounts.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/** Account detail route: shows one account and its profiles (free or occupied). */
@Composable
fun AccountDetailRoute(
    accountId: String,
    onBack: () -> Unit,
    onEditAccount: (String) -> Unit,
    onAssignProfile: (accountId: String, profileId: String) -> Unit,
    viewModel: AccountDetailViewModel = koinViewModel(key = accountId) { parametersOf(accountId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AccountDetailEvent.NavigateBack -> onBack()
                is AccountDetailEvent.NavigateToEdit -> onEditAccount(event.accountId)
                is AccountDetailEvent.NavigateToAssign -> onAssignProfile(event.accountId, event.profileId)
            }
        }
    }

    AccountDetailScreen(state = state, onIntent = viewModel::onIntent)
}
