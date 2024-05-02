package com.michaelrichards.laughlounge.utils

import com.michaelrichards.laughlounge.controller.IMAGE_CONTROLLER_BASE_PATH_V1
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.util.UUID


@Component
class EnvironmentUtil(
    private val environment: Environment,
    @Value("\${server.port}")
    private var port: Int
) {


    private fun getHostname(): String =  InetAddress.getLocalHost().hostAddress


    private fun getServerUrlPrefi() = "http://${getHostname()}:${port}"

    fun buildImageLink(uuid: UUID) = "${getServerUrlPrefi()}/$IMAGE_CONTROLLER_BASE_PATH_V1/$uuid"

}