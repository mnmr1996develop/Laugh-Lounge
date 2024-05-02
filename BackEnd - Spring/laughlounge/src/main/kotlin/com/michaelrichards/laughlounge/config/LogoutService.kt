package com.michaelrichards.laughlounge.config

import com.michaelrichards.laughlounge.repositories.TokenRepository
import com.michaelrichards.laughlounge.utils.Variables
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class LogoutService(
    private val tokenRepository: TokenRepository
) : LogoutHandler {

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val authHeader = request?.getHeader(Variables.AUTHORIZATION)
        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(Variables.BEARER)) {

            return
        }
        val jwt: String = authHeader.substring(7)
        val storedToken = tokenRepository.findByToken(jwt)

        if (storedToken != null) {
            storedToken.isRevoked = true
            storedToken.isExpired= true
            tokenRepository.save(storedToken)
        }
    }

}