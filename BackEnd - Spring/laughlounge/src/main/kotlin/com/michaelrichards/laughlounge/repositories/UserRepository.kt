package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID>{

    fun findByUsername(username: String): User?
    fun findByUsernameIgnoreCase(username: String): User?


    fun findByEmailIgnoreCase(email: String): User?
}

