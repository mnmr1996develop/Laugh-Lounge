package com.michaelrichards.laughlounge.domain.responses

data class UserFollowRequestResponse(
    val username: String,
    val followRequestCount: Long,
    val responsesPerPage: Long,
    val pageNumber: Int,
    val userDetailsResponse: List<UserFollowRequestDataResponse>
)

