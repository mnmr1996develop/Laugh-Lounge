package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.service.BlockedService
import com.michaelrichards.laughlounge.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private const val USER_BASE_PATH_V1 = "api/v1/users"

@RestController
@RequestMapping(USER_BASE_PATH_V1)
class UserController(
    private val userService: UserService,
    private val blockService: BlockedService
) {

    @GetMapping("{username}")
    fun getUserByUsername(
        @AuthenticationPrincipal user: User,
        @PathVariable("username") username: String
    ): ResponseEntity<UserDetailsResponse> = ResponseEntity.ok().body(userService.getUserDetailsResponse(user = user,username= username))



    @PostMapping("{username}")
    fun uploadProfileImage(
        @AuthenticationPrincipal user: User,
        @RequestParam("profile-image") file: MultipartFile
    ): ResponseEntity<UserDetailsResponse> = ResponseEntity.ok().body(
        userService.uploadProfileImage(user, file)
    )

    @PostMapping("{username}/block")
    fun blockUser(
        @AuthenticationPrincipal user: User,
        @PathVariable("username") username: String
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().body(
            blockService.blockUser(user = user, blockingUsername = username)
        )

    }

    @PostMapping("{username}/unblock")
    fun unblockUser(
        @AuthenticationPrincipal user: User,
        @PathVariable("username") username: String
    ){
        blockService.unblockUser(user = user, blockedUsername = username)
    }

    @PutMapping
    fun makeToggleProfilePrivacy(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<UserDetailsResponse> =
        ResponseEntity.ok().body(userService.togglePrivacy(user = user))
}