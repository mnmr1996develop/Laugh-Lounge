package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.following.BlockedUsers
import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BlockedUserRepository: JpaRepository<BlockedUsers, UUID> {


    fun existsByUserBlockedAndBlockedBy(userBlocked: User, blockedBy: User): Boolean


}