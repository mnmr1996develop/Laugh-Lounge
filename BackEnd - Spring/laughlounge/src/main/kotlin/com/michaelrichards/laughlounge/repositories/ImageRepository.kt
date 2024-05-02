package com.michaelrichards.laughlounge.repositories

import com.michaelrichards.laughlounge.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ImageRepository: JpaRepository<Image, UUID> {

}