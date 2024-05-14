package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.domain.responses.UserFollowRequestDataResponse
import com.michaelrichards.laughlounge.domain.responses.UserFollowRequestResponse
import com.michaelrichards.laughlounge.domain.responses.UserFollowResponse
import com.michaelrichards.laughlounge.model.following.FollowRelations
import com.michaelrichards.laughlounge.model.following.FollowRequest
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.repositories.*
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class FollowService(
    private val followRelationsRepository: FollowRelationsRepository,
    private val followRequestRepository: FollowRequestRepository,
    private val blockUserRepository: BlockedUserRepository,
    private val environmentUtil: EnvironmentUtil,
    private val userRepository: UserRepository,
    private val userService: UserService, private val trackedUserDataRepository: TrackedUserDataRepository
) {


    fun follow(
        follower: User,
        followingUsername: String
    ) {

        val following = userService.findByUsername(followingUsername)

        if (follower.userId == null || following.userId == null) throw Exception()

        val isFollowing =
            followRelationsRepository.existsByFollowerAndFollowing(follower, following)

        val followRequestAlreadyExist =
            followRequestRepository.existsByFollowRequesterAndFollowRequestee(follower, following)



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
            } else throw Exception()
        } else {

            if (!isFollowing && !followRequestAlreadyExist) {

                val followRequest = FollowRequest(
                    followRequester = follower,
                    followRequestee = following,
                    timeRequestSent = LocalDateTime.now()
                )

                follower.followRequestsSent.add(followRequest)
                following.followRequests.add(followRequest)
                userRepository.save(following)
                userRepository.save(follower)
                followRequestRepository.save(followRequest)

            } else if (followRequestAlreadyExist) throw Exception()
            else throw Exception()

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

            //find by who is following user
            followRelationsRepository.findByFollowing_UserId(user.userId, pageRequest).map { followRelation ->

                //caller should be following here, followe
                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRelation.following!!,
                    followRelation.follower!!
                )

                User.mapToUserDetailsDto(
                    followRelation.follower!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
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
                //caller should be following here
                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRelation.following!!,
                    followRelation.follower!!
                )

                User.mapToUserDetailsDto(
                    followRelation.follower!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
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

    fun getFollowing(
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

            followRelationsRepository.findByFollower_UserId(user.userId, pageRequest).map { followRelation ->


                //caller should be follower here
                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRelation.follower!!,
                    followRelation.following!!
                )

                User.mapToUserDetailsDto(
                    followRelation.follower!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
                    callerIsFollowing = callerIsFollowingUser
                )
            }

        }

        return UserFollowResponse(
            username = user.username,
            followerCount = followRelationsRepository.countByFollower_UserId(user.userId!!),
            userDetailsResponse = relations,
            responsesPerPage = pageSize.toLong(),
            pageNumber = pageNumber
        )
    }

    fun getFollowing(
        user: User,
        ascending: Boolean
    ): UserFollowResponse {

        if (user.userId == null) throw Exception()

        val relations: List<UserDetailsResponse> = user.userId.let { it2 ->
            followRelationsRepository.findByFollower_UserId(it2).map { followRelation: FollowRelations ->
                //caller should be follower here
                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRelation.follower!!,
                    followRelation.following!!
                )

                User.mapToUserDetailsDto(
                    followRelation.follower!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
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

    fun unfollowUser(follower: User, followingUsername: String): Unit {
        val following = userService.findByUsername(followingUsername)
        val followRelations =
            followRelationsRepository.findByFollowerAndFollowing(follower = follower, following = following)

        val trackedUserData = trackedUserDataRepository.findByUser(follower)

        trackedUserData?.let {
            it.lastRequest = LocalDateTime.now()
            it.amountOfAPIRequest += 1
            trackedUserDataRepository.save(it)
        }


        followRelations?.let {
            follower.following.remove(it)
            following.followers.remove(it)
            followRelationsRepository.delete(it)
        } ?: throw Exception()

    }


    fun getFollowersRequest(
        user: User,
        pageNumber: Int,
        pageSize: Int = 20,
        isAscending: Boolean
    ): UserFollowRequestResponse {

        val relations: List<UserFollowRequestDataResponse> = pageNumber.let { it1 ->

            val pageRequest =
                if (isAscending) PageRequest.of(it1, pageSize, Sort.by("timeFollowed").ascending()) else PageRequest.of(
                    it1,
                    pageSize,
                    Sort.by("timeFollowed").descending()
                )


            if (user.userId == null) throw Exception()


            followRequestRepository.findByFollowRequestee(user, pageRequest).map { followRequest ->

                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRequest.followRequestee!!,
                    followRequest.followRequester!!
                )

                User.mapToFollowRequestDto(
                    followRequest.followRequester!!,
                    requestId = followRequest.id!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
                    callerIsFollowing = callerIsFollowingUser
                )
            }

        }

        return UserFollowRequestResponse(
            username = user.username,
            followRequestCount = followRequestRepository.countByFollowRequestee(user),
            userDetailsResponse = relations,
            responsesPerPage = pageSize.toLong(),
            pageNumber = pageNumber
        )
    }

    fun getFollowersRequest(
        user: User,
        ascending: Boolean
    ): UserFollowRequestResponse {

        if (user.userId == null) throw Exception()

        val relations: List<UserFollowRequestDataResponse> =


            followRequestRepository.findByFollowRequestee(user).map { followRequest ->

                val (callerIsFollowingUser, userIsFollowingCaller) = getMutualFollowRelation(
                    followRequest.followRequestee!!,
                    followRequest.followRequester!!
                )

                User.mapToFollowRequestDto(
                    followRequest.followRequester!!,
                    requestId = followRequest.id!!,
                    environmentUtil,
                    isFollowingCaller = userIsFollowingCaller,
                    callerIsFollowing = callerIsFollowingUser
                )
            }

        val count = followRequestRepository.countByFollowRequestee(user)

        return UserFollowRequestResponse(
            username = user.username,
            followRequestCount = count,
            userDetailsResponse = relations,
            responsesPerPage = count,
            pageNumber = 0
        )
    }

    private fun getMutualFollowRelation(user1: User, user2: User): Pair<Boolean, Boolean> {

        val user1IsFollowingUser2: Boolean =
            followRelationsRepository.existsByFollowerAndFollowing(
                follower = user1,
                following = user2
            )

        val user2IsFollowingUser1: Boolean =
            followRelationsRepository.existsByFollowerAndFollowing(
                following = user2,
                follower = user1
            )

        return Pair(user1IsFollowingUser2, user2IsFollowingUser1)

    }

    @Transactional
    fun acceptFollowRequest(user: User, requestId: UUID): Unit {
        val followRequest = followRequestRepository.findById(requestId).orElseThrow {
            Exception()
        }
        if (followRequest.followRequestee != user) throw Exception()

        val followRelation = FollowRelations(
            following = user,
            follower = followRequest.followRequester!!,
            timeFollowed = LocalDateTime.now()
        )

        user.followRequests.remove(followRequest)
        user.followers.add(followRelation)

        followRequest.followRequester!!.followRequestsSent.remove(followRequest)
        followRequest.followRequester!!.following.add(followRelation)

        userRepository.save(user)
        userRepository.save(followRequest.followRequester!!)

        followRequestRepository.delete(followRequest)
        followRelationsRepository.save(followRelation)


    }


}