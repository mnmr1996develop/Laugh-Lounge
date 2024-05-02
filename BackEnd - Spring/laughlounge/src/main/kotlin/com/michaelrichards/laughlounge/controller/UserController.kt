package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.service.AuthenticationService
import com.michaelrichards.laughlounge.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

private const val USER_BASE_PATH_V1 = "api/v1/users"

@RestController
@RequestMapping(USER_BASE_PATH_V1)
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDetailsResponse>> =
        ResponseEntity.ok().body(userService.getAllUsers())


    @GetMapping("{username}")
    fun getUserByUsername(
        @PathVariable("username") username: String
    ): ResponseEntity<UserDetailsResponse> = ResponseEntity.ok().body(userService.getUserDetailsResponse(username))

    @PostMapping("{username}")
    fun uploadProfileImage(
        @PathVariable("username") username: String,
        @RequestParam("profile-image") file: MultipartFile
    ): ResponseEntity<UserDetailsResponse> = ResponseEntity.ok().body(
        userService.uploadProfileImage(username, file)
    )
}