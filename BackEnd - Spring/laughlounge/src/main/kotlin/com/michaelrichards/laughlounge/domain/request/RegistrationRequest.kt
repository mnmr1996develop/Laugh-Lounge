package com.michaelrichards.laughlounge.domain.request

import java.time.LocalDate

data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val birthday: LocalDate,
    val password: String
) {
}