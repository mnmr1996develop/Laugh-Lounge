package com.michaelrichards.laughlounge.model

import jakarta.persistence.*
import java.util.UUID

@Entity
class Token(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    var token: String =  "",

    @Enumerated(value = EnumType.STRING)
    val tokenType: TokenType = TokenType.BEARER,

    var isExpired: Boolean = false,

    var isRevoked: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
) {

    fun isTokenValid() = !isRevoked && !isExpired



}

enum class TokenType {
    BEARER
}