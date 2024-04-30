package com.michaelrichards.laughlounge.domain.responses

data class PostResponse(
    val text: String,
    val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostResponse

        if (text != other.text) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}