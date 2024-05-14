package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.following.FollowRequest
import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FollowRequestRepository: JpaRepository<FollowRequest, UUID> {


    fun existsByFollowRequesterAndFollowRequestee(followRequester: User, followRequestee: User): Boolean


    fun findByFollowRequestee(followRequestee: User, pageable: Pageable): List<FollowRequest>

    fun findByFollowRequestee(followRequestee: User): List<FollowRequest>


    fun countByFollowRequestee(followRequestee: User): Long


}