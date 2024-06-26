package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.following.FollowRelations
import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FollowRelationsRepository: JpaRepository<FollowRelations, UUID> {





    fun existsByFollowerAndFollowing(follower: User, following: User): Boolean


    fun findByFollowing_UserId(userId: Long): List<FollowRelations>

    @Query("select f from FollowRelations f where f.following.userId = ?1")
    fun findByFollowing_UserId(userId: Long, pageable: Pageable): List<FollowRelations>


    @Query("select f from FollowRelations f where f.follower.userId = ?1")
    fun findByFollower_UserId(userId: Long, pageable: Pageable): List<FollowRelations>


    fun findByFollower_UserId(userId: Long): List<FollowRelations>


    @Query("select f from FollowRelations f where f.follower = ?1 and f.following = ?2")
    fun findByFollowerAndFollowing(follower: User, following: User): FollowRelations?


    fun countByFollowing_UserId(userId: Long): Long


    fun countByFollower_UserId(userId: Long): Long
}