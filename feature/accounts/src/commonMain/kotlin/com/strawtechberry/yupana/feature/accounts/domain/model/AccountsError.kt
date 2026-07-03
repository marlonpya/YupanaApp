package com.strawtechberry.yupana.feature.accounts.domain.model

/**
 * Errors shared by the accounts/services/profiles operations in this module — one
 * error type for the whole feature (not one per entity) since Service/Account/Profile
 * are three closely related entities inside a single module.
 */
sealed interface AccountsError {
    data object NoConnection : AccountsError
    data object DuplicateServiceName : AccountsError
    data object ServiceInUse : AccountsError
    data class Unknown(val detail: String?) : AccountsError
}

/** Exception that carries an [AccountsError] through `Result.failure`. */
class AccountsException(val error: AccountsError) : Exception()
