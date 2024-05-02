package com.michaelrichards.laughlounge.model

import com.michaelrichards.laughlounge.domain.responses.PostResponse
import jakarta.persistence.*
import java.util.UUID

@Entity
class Post(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var postId: UUID? = null,

    @Column(name = "text", nullable = true)
    var text: String? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "image_uuid")
    var image: Image? = null,

    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "post_author_id")
    var postAuthor: User? = null

) {

    companion object {
        fun mapToDTO(post: Post): PostResponse = PostResponse(
            text = post.text,
            imageLink = post.image?.uuid!!
        )
    }



}