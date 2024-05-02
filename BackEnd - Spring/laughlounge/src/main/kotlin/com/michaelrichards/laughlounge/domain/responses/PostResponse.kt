package com.michaelrichards.laughlounge.domain.responses

import java.io.Serializable
import java.util.UUID

data class PostResponse(
    val text: String?,
    val imageLink: UUID?
): Serializable {
}