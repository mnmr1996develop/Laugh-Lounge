package com.michaelrichards.laughlounge.domain.responses

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)