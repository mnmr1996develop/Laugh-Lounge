package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


private const val ADMIN_BASE_PATH_V1 = "api/v1/admin"

@RestController
@RequestMapping(ADMIN_BASE_PATH_V1)
class AdminController(
    private val userService: UserService
) {

    @RequestMapping("ban")
    fun banUser(
        @RequestParam username: String
    ) = userService.banUser(username)

    @RequestMapping("unban")
    fun unbanUser(
        @RequestParam username: String
    ) = userService.banUser(username)


}