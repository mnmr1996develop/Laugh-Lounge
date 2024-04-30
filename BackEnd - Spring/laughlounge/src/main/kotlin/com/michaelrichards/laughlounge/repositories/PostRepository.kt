package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.Post
import com.michaelrichards.laughlounge.model.mUser
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<Post, Long>{


    fun findByMUser(mUser: mUser): List<Post>


    fun findByMUserPageable(mUser: mUser, pageable: Pageable): List<Post>
}