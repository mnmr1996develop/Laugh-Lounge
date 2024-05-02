package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.service.ImageService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

const val IMAGE_CONTROLLER_BASE_PATH_V1 = "api/v1/images"

@RestController
@RequestMapping(IMAGE_CONTROLLER_BASE_PATH_V1)
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("{fileName}")
    fun getImage(
        @PathVariable fileName: UUID
    ): ResponseEntity<Resource>{

        val (image, type) = imageService.downloadImage(fileName)

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(type))
            .body(ByteArrayResource(image))
    }
}