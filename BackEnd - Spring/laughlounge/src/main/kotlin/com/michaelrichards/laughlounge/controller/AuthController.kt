package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val AUTH_BASE_PATH = "api/v1/auth"

@RestController
@RequestMapping(AUTH_BASE_PATH)
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun registerUser(
        @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<UserDetailsResponse> = ResponseEntity.ok().body(authenticationService.registerUser(registrationRequest))

}