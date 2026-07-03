package com.strawtechberry.yupana.feature.assignment.domain.model

/** Where an assignment currently lives — needed to find same-service destination accounts. */
data class AssignmentMoveContext(
    val profileId: String,
    val accountId: String,
    val serviceId: String,
)
