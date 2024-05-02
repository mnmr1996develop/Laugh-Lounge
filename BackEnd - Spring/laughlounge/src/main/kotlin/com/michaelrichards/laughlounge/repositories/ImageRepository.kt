package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ImageRepository: JpaRepository<Image, UUID> {

}