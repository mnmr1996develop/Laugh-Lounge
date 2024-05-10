package com.michaelrichards.laughlounge.model.following

import com.michaelrichards.laughlounge.model.user.User
import jakarta.persistence.*
import java.util.UUID


@Entity
class BlockedUsers(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_blocked_user_id")
    var userBlocked: User? = null,

    @ManyToOne
    @JoinColumn(name = "blocked_by_user_id")
    var blockedBy: User? = null,

) {


}