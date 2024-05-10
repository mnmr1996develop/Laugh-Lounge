package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.domain.responses.UserFollowResponse
import com.michaelrichards.laughlounge.model.following.FollowRelations
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.repositories.BlockedUserRepository
import com.michaelrichards.laughlounge.repositories.FollowRelationsRepository
import com.michaelrichards.laughlounge.repositories.FollowRequestRepository
import com.michaelrichards.laughlounge.repositories.UserRepository
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class FollowService(
    private val followRelationsRepository: FollowRelationsRepository,
    private val followRequestRepository: FollowRequestRepository,
    private val blockUserRepository: BlockedUserRepository,
    private val environmentUtil: EnvironmentUtil,
    private val userRepository: UserRepository,
    private val userService: UserService
) {


    fun follow(
        follower: User,
        followingUsername: String
    ) {

        val following = userService.findByUsername(followingUsername)

        if (follower.userId == null || following.userId == null) throw Exception()

        val isFollowing =
            followRelationsRepository.existsByFollower_UserIdAndFollowing_UserId(follower.userId, following.userId)





        if (following.isProfilePublic) {
            if (!isFollowing) {
                val followRelations = FollowRelations(
                    follower = follower,
                    following = following,
                    timeFollowed = LocalDateTime.now()
                )

                follower.following.add(followRelations)
                following.followers.add(followRelations)
                userRepository.save(following)
                userRepository.save(follower)
                followRelationsRepository.save(followRelations)

                println(followRelations)
            } else throw Exception()
        } else {

        }


    }

    fun getFollowers(
        user: User,
        pageNumber: Int,
        pageSize: Int = 20,
        isAscending: Boolean
    ): UserFollowResponse {

        val relations: List<UserDetailsResponse> = pageNumber.let { it1 ->

            val pageRequest =
                if (isAscending) PageRequest.of(it1, pageSize, Sort.by("timeFollowed").ascending()) else PageRequest.of(
                    it1,
                    pageSize,
                    Sort.by("timeFollowed").descending()
                )


            if (user.userId == null) throw Exception()

            followRelationsRepository.findByFollowing_UserId(user.userId, pageRequest).map { followRelation ->

                //we know these are the followers of the user but this is to see if the relation goes the other way as well
                val callerIsFollowingUser: Boolean =
                    followRelationsRepository.existsByFollower_UserIdAndFollowing_UserId(
                        following = followRelation.follower?.userId!!,
                        follower = followRelation.following?.userId!!
                    )
                User.mapToDto(
                    followRelation.follower!!,
                    environmentUtil,
                    isFollowingCaller = true,
                    callerIsFollowing = callerIsFollowingUser
                )
            }

        }

        return UserFollowResponse(
            username = user.username,
            followerCount = followRelationsRepository.countByFollowing_UserId(user.userId!!),
            userDetailsResponse = relations,
            responsesPerPage = pageSize.toLong(),
            pageNumber = pageNumber
        )
    }

    fun getFollowers(
        user: User,
        ascending: Boolean
    ): UserFollowResponse {

        if (user.userId == null) throw Exception()

        val relations: List<UserDetailsResponse> = user.userId.let { it2 ->
            followRelationsRepository.findByFollowing_UserId(it2).map { followRelation: FollowRelations ->
                val callerIsFollowingUser: Boolean =
                    followRelationsRepository.existsByFollower_UserIdAndFollowing_UserId(
                        following = followRelation.follower?.userId!!,
                        follower = followRelation.following?.userId!!
                    )
                User.mapToDto(
                    followRelation.following!!,
                    environmentUtil,
                    isFollowingCaller = true,
                    callerIsFollowing = callerIsFollowingUser
                )
            }
        }

        val count = followRelationsRepository.countByFollowing_UserId(user.userId)

        return UserFollowResponse(
            username = user.username,
            followerCount = count,
            userDetailsResponse = relations,
            responsesPerPage = count,
            pageNumber = 0
        )
    }


    fun unfollowUser(follower: User, followingUsername: String): Unit? {
        TODO()
    }

    fun getFollowersRequest(user: User, pageNumber: Int?, ascending: Boolean): Any {
        TODO()
    }


}