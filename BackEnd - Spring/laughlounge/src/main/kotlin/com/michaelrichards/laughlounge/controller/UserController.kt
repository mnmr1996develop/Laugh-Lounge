package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.service.AuthenticationService
import com.michaelrichards.laughlounge.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val USER_BASE_PATH_V1 = "api/v1/users"

@RestController
@RequestMapping(USER_BASE_PATH_V1)
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDetailsResponse>> =
        ResponseEntity.ok().body(userService.getAllUsers())


}