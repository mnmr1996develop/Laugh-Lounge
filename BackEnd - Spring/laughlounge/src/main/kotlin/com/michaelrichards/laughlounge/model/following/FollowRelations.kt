package com.michaelrichards.laughlounge.model.following

import com.michaelrichards.laughlounge.model.user.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(
    name = "follow_relations",
    uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("follower_user_id", "following_user_id"))]
)
class FollowRelations(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "follower_user_id", nullable = false)
    var follower: User? = null,


    @ManyToOne(optional = false)
    @JoinColumn(name = "following_user_id", nullable = false)
    var following: User? = null,

    var timeFollowed: LocalDateTime

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FollowRelations

        if (id != other.id) return false
        if (timeFollowed != other.timeFollowed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + timeFollowed.hashCode()
        return result
    }

    override fun toString(): String {
        return "FollowRelations(id=$id, follower=${follower?.username}, following=${following?.username}, timeFollowed=$timeFollowed)"
    }


}
