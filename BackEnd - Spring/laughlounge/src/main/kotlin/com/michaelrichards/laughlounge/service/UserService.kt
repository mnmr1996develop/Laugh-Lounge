package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers(): List<UserDetailsResponse> = userRepository.findAll().map {
        it.mapToDto()
    }



}