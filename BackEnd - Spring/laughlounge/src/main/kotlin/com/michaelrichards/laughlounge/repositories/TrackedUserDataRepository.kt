package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.user.TrackedUserData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrackedUserDataRepository : JpaRepository<TrackedUserData, UUID> {


    fun findByUser_UserId(userId: Long): TrackedUserData?

}