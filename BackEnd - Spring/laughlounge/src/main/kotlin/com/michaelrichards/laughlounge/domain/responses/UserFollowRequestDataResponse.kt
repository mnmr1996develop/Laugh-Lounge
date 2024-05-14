package com.michaelrichards.laughlounge.domain.responses

import java.time.LocalDate
import java.util.*

data class UserFollowRequestDataResponse(
    val requestId: UUID,
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