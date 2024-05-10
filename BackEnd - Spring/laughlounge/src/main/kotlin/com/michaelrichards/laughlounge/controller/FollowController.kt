package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.domain.responses.UserFollowResponse
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.service.FollowService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private const val FOLLOWING_BASE_PATH_V1 = "api/v1/followers"

@RestController
@RequestMapping(FOLLOWING_BASE_PATH_V1)
class FollowController(
    private val followService: FollowService
) {

    @PostMapping("/follow")
    fun followUser(
        @AuthenticationPrincipal user: User,
        @RequestParam("username") username: String
    ): ResponseEntity<Unit> = ResponseEntity.ok().body(followService.follow(
        follower = user,
        followingUsername = username
    ))

    @PostMapping("/unfollow")
    fun unfollowUser(
        @AuthenticationPrincipal user: User,
        @RequestParam("username") username: String
    ) : ResponseEntity<Unit> = ResponseEntity.ok().body(
        followService.unfollowUser(
            follower = user,
            followingUsername = username
        )
    )

    @GetMapping
    fun getMyFollowers(
        @AuthenticationPrincipal user: User,
        @RequestParam pageNumber: Int? = null,
        @RequestParam pageSize: Int = 20,
        @RequestParam isAscending: Boolean = false,
    ) : ResponseEntity<UserFollowResponse> = ResponseEntity.ok().body(


        if(pageNumber ==  null){
            followService.getFollowers(user, isAscending)
        }else {
            followService.getFollowers(user = user, pageNumber = pageNumber, pageSize = 20, isAscending = isAscending)
        }
    )

    @GetMapping("request")
    fun getMyFollowRequest(
        @AuthenticationPrincipal user: User,
        @RequestParam pageNumber: Int? = null,
        @RequestParam isAscending: Boolean = true
    ) = ResponseEntity.ok().body(
        followService.getFollowersRequest(
            user = user,
            pageNumber = pageNumber,
            ascending = isAscending
        )
    )



}