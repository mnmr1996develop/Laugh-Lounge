package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>{

    fun findByUsername(username: String): User?
    fun findByUsernameIgnoreCase(username: String): User?


    @Query(
        """select u from User u inner join u.usersBlockedMe usersBlockedMe
where u.username = ?1 and u not in ?2"""
    )
    fun findByUsernameExcludingBanList(username: String, blockList: MutableCollection<User>): User


    fun existsByEmailIgnoreCase(email: String): Boolean


    fun existsByUsernameIgnoreCase(username: String): Boolean
}

