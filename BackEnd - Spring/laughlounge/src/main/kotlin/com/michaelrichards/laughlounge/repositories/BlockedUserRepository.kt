package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.following.BlockedUsers
import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BlockedUserRepository: JpaRepository<BlockedUsers, UUID> {


    fun existsByUserBlockedAndBlockedBy(userBlocked: User, blockedBy: User): Boolean


    fun findByUserBlocked(userBlocked: User): List<BlockedUsers>


    fun findByBlockedBy(blockedBy: User): List<BlockedUsers>


    @Query("select b from BlockedUsers b where b.userBlocked = ?1 or b.blockedBy = ?2")
    fun findByUserBlockedOrBlockedBy(userBlocked: User, blockedBy: User): List<BlockedUsers>


}