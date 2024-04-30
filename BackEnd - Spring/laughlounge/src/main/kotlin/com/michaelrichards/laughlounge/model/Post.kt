package com.michaelrichards.laughlounge.model

import jakarta.persistence.*

@Entity
class Post(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var postId: Long? = null,

    @Column(name = "text", nullable = true)
    var text: String? = null,

    @Lob
    @Column(name = "image_data", length = 1000)
    var image: ByteArray? = null,

    @ManyToOne(cascade = [CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "m_user_id")
    val mUser: mUser? = null
) {

    companion object {
        fun mapToDTO(post: Post){

        }
    }
}