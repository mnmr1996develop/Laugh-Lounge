package com.michaelrichards.laughlounge.domain.responses

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class APIException(
    val status: HttpStatus,
    val message: String,
    val reason : String,
    val timeStamp: LocalDateTime
)
