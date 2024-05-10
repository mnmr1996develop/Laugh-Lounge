package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.repositories.TrackedUserDataRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TrackedUserDataService(
    private val trackedUserDataRepository: TrackedUserDataRepository,
) {

    fun findById(id: UUID) = trackedUserDataRepository.findById(id).orElseThrow {
        Exception()
    }

    fun userMadeAPICall(
        id: UUID
    ) {
        val data = findById(id)
        data.amountOfAPIRequest += 1
        trackedUserDataRepository.save(data)
    }
}