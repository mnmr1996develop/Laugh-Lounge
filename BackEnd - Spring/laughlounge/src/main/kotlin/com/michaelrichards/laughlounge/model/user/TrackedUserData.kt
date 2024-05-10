package com.michaelrichards.laughlounge.model.user

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class TrackedUserData(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column
    var lastRequest: LocalDateTime? = null,

    @Column
    var amountOfAPIRequest: Long= 0,

    @OneToOne(
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH],
        optional = false,
        orphanRemoval = true
    )
    @JoinColumn(name = "user_user_id", nullable = false)
    var user: User? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackedUserData

        if (id != other.id) return false
        if (lastRequest != other.lastRequest) return false
        if (amountOfAPIRequest != other.amountOfAPIRequest) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (lastRequest?.hashCode() ?: 0)
        result = 31 * result + amountOfAPIRequest.hashCode()
        result = 31 * result + (user?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TrackedUserData(lastRequest=$lastRequest, id=$id, amountOfAPIRequest=$amountOfAPIRequest, user=${user?.username})"
    }


}