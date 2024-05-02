package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.repositories.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {

}