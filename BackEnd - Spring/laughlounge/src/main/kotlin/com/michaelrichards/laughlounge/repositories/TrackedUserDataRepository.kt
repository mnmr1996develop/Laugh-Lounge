package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.user.TrackedUserData
import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrackedUserDataRepository : JpaRepository<TrackedUserData, UUID> {


    fun findByUser(user: User): TrackedUserData?


}