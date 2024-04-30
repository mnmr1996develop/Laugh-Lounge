package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.mUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<mUser, UUID>{

    fun findByUsername(username: String): mUser?
}

