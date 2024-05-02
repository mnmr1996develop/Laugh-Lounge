package com.michaelrichards.laughlounge.controller

import com.michaelrichards.laughlounge.service.PostService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val AUTH_BASE_PATH = "api/v1/posts"

@RestController
@RequestMapping()
class PostController(
    private val postService: PostService
) {


}