package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenRepository : JpaRepository<Token, UUID> {

    @Query("select t from Token t inner join User u on t.user.userId = u.id where u.id = :userId and (t.isExpired = false or t.isRevoked = false)")
    fun findAllValidTokensByUser(userId: UUID): List<Token>

    fun findByToken(token: String): Token?
}