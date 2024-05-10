package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.following.FollowRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FollowRequestRepository: JpaRepository<FollowRequest, UUID> {
}