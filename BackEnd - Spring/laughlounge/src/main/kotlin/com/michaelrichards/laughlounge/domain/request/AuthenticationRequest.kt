package com.michaelrichards.laughlounge.domain.request

data class AuthenticationRequest(
    val username: String,
    val password: String
)