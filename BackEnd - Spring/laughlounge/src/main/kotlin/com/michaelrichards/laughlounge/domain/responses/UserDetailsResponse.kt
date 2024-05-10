package com.michaelrichards.laughlounge.domain.responses

import java.time.LocalDate

data class UserDetailsResponse(
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val birthday: LocalDate,
    val isProfilePublic: Boolean,
    val profileImageLink: String?,
    val isFollowingCaller: Boolean,
    val callerIsFollowing: Boolean
)
