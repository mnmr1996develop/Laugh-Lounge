package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.following.FollowRelations
import com.michaelrichards.laughlounge.model.following.FollowRequest
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.repositories.FollowRelationsRepository
import com.michaelrichards.laughlounge.repositories.FollowRequestRepository
import com.michaelrichards.laughlounge.repositories.UserRepository
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val imageService: ImageService,
    private val environmentUtil: EnvironmentUtil,
    private val followRequestRepository: FollowRequestRepository,
    private val followRelationsRepository: FollowRelationsRepository
) : UserDetailsService {

    fun findByUsername(username: String) =
        userRepository.findByUsername(username = username) ?: throw Exception()


    fun getUserDetailsResponse(user: User, username: String): UserDetailsResponse {
        val userRequested = findByUsername(username)

        if (user.userId == null || userRequested.userId == null) throw Exception()

        val isUserFollowingCaller = followRelationsRepository.existsByFollower_UserIdAndFollowing_UserId(follower = userRequested.userId, following = user.userId)
        val isCallerFollowingUser = followRelationsRepository.existsByFollower_UserIdAndFollowing_UserId(follower = user.userId, following = userRequested.userId)
        return User.mapToDto(userRequested, environmentUtil, isFollowingCaller = isUserFollowingCaller, isCallerFollowingUser)
    }


   /* fun getAllUsers(): List<UserDetailsResponse> = userRepository.findAll().map {
        User.mapToDto(it, environmentUtil)
    }*/

    fun banUser(username: String): Boolean {
        userRepository.findByUsername(username)

        // TODO: Implement a banning account system
        return true
    }

    /*@Transactional
    fun uploadProfileImage(username: String, file: MultipartFile): UserDetailsResponse {
            val image = imageService.uploadImage(file)
            val user = findByUsername(username)
            user.profileImage = image
            return mUser.mapToDto(userRepository.save(user), user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) })
    }*/

    @Transactional
    fun uploadProfileImage(user: User, file: MultipartFile): UserDetailsResponse {
        return if (user.profileImage == null){
            val image = imageService.uploadImage(file)
            user.profileImage = image
            User.mapToDto(userRepository.save(user), environmentUtil, true, true)
        }else {
            user.profileImage?.let {
                imageService.changeImage(file, it)
            }
            User.mapToDto(userRepository.save(user), environmentUtil, true, true)
        }
    }

    override fun loadUserByUsername(username: String): UserDetails = findByUsername(username)


    @Transactional
    fun togglePrivacy(user: User): UserDetailsResponse {
        if (!user.isProfilePublic){
             user.followRequests.forEach { followRequest: FollowRequest ->
                 val followRelations = FollowRelations(
                     following = followRequest.followRequestee,
                     follower = followRequest.followRequester,
                     timeFollowed = LocalDateTime.now()
                 )
                 followRelationsRepository.save(followRelations)
                 followRequestRepository.delete(followRequest)
             }
            user.followRequests = mutableListOf()

        }
        user.isProfilePublic = !user.isProfilePublic
        userRepository.save(user)
        return User.mapToDto(user, environmentUtil, true, true)
    }


}