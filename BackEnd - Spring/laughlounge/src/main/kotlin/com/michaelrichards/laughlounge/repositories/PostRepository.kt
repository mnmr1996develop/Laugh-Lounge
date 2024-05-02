package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.Post
import com.michaelrichards.laughlounge.model.mUser
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PostRepository: JpaRepository<Post, UUID>{

}