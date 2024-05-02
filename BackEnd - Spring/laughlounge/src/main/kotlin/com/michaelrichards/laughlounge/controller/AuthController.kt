package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.request.AuthenticationRequest
import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.AuthenticationResponse
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
    ): ResponseEntity<AuthenticationResponse> = ResponseEntity.ok().body(authenticationService.registerUser(registrationRequest))

    @PostMapping("/login")
    fun login(
        @RequestBody registerRequest: AuthenticationRequest
    ): ResponseEntity<AuthenticationResponse> = ResponseEntity.ok(authenticationService.login(registerRequest))

    @PostMapping("/refresh-token")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) = authenticationService.refreshToken(request, response)

}