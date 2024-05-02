package com.michaelrichards.laughlounge.model

import jakarta.persistence.*
import java.util.UUID

@Entity
class Image(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    var uuid: UUID? = null,

    @Lob
    @Column(name = "image_data")
    var imageData: ByteArray? = null,

    @Column(name = "type")
    var type: String = "",
) {
}