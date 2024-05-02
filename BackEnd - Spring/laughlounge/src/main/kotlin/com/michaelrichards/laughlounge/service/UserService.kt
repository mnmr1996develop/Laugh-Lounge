package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.mUser
import com.michaelrichards.laughlounge.repositories.UserRepository
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val userRepository: UserRepository,
    private val environmentUtil: EnvironmentUtil,
    private val imageService: ImageService
) {

    private fun findByUsername(username: String) =
        userRepository.findByUsername(username = username) ?: throw Exception()

    fun getUserDetailsResponse(username: String): UserDetailsResponse {
        val user = findByUsername(username)
        return mUser.mapToDto(user, user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) })
    }


    fun getAllUsers(): List<UserDetailsResponse> = userRepository.findAll().map {
        mUser.mapToDto(it, it.profileImage?.uuid?.let { uuid -> environmentUtil.buildImageLink(uuid) })
    }

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
    fun uploadProfileImage(username: String, file: MultipartFile): UserDetailsResponse {
        val user = findByUsername(username)

        return if (user.profileImage == null){
            val image = imageService.uploadImage(file)
            user.profileImage = image
            mUser.mapToDto(userRepository.save(user), user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) })
        }else {
            user.profileImage?.let {
                imageService.changeImage(file, it)
            }
            mUser.mapToDto(userRepository.save(user), user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) })
        }


    }


}