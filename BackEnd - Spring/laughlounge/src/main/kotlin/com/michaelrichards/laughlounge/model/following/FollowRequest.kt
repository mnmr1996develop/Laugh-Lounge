package com.michaelrichards.laughlounge.model.following

import com.michaelrichards.laughlounge.model.user.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "follow_request",
    uniqueConstraints = [UniqueConstraint(
        columnNames = arrayOf(
            "follow_requester_user_id",
            "follow_requestee_user_id"
        )
    )]
)
class FollowRequest(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,


    @ManyToOne(optional = false)
    @JoinColumn(name = "follow_requester_user_id")
    var followRequester: User? = null,


    @ManyToOne(optional = false)
    @JoinColumn(name = "follow_requestee_user_id")
    var followRequestee: User? = null,

    val timeRequestSent: LocalDateTime
) {


}