package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.repositories.BlockedUserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BlockedService(
    private val blockedUserRepository: BlockedUserRepository
) {

    fun blockUser(user: User, blockingUsername: String){

    }

    fun unblockUser(user: User, blockedUsername: String){

    }

}