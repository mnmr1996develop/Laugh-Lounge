package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.model.Image
import com.michaelrichards.laughlounge.repositories.ImageRepository
import com.michaelrichards.laughlounge.utils.ImageUtils
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Repository
class ImageService(
    private val imageRepository: ImageRepository
) {

    fun uploadImage(file: MultipartFile): Image {
        val image = Image(
            type = file.contentType!!,
            imageData = ImageUtils.compressImage(file.bytes)
        )
        return imageRepository.save(image)
    }

    fun changeImage(file: MultipartFile, image: Image): Image{
        image.imageData = ImageUtils.compressImage(file.bytes)
        image.type = file.contentType!!
        return imageRepository.save(image)
    }

    fun downloadImage(id: UUID): Pair<ByteArray, String>{
        val image = imageRepository.findById(id).orElseThrow{Exception()}

        return Pair(ImageUtils.decompressImage(image.imageData ?: throw Exception()), image.type)
    }

}