package com.michaelrichards.laughlounge.domain.responses

data class UserFollowResponse(
    val username: String,
    val followerCount: Long,
    val responsesPerPage: Long,
    val pageNumber: Int,
    val userDetailsResponse: List<UserDetailsResponse>
)