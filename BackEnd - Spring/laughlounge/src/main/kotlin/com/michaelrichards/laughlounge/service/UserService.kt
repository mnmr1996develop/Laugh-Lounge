package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.mUser
import com.michaelrichards.laughlounge.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserByUsername(username: String): UserDetailsResponse =
        mUser.mapToDto(userRepository.findByUsername(username) ?: throw Exception())

    fun getAllUsers(): List<UserDetailsResponse> = userRepository.findAll().map {
        mUser.mapToDto(it)
    }

    fun banUser(username: String): Boolean {
        userRepository.findByUsername(username)

        // TODO: Implement a banning account system
        return true
    }


}