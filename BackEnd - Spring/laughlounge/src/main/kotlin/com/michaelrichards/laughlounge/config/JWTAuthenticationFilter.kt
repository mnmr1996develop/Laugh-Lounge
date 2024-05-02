package com.michaelrichards.laughlounge.config

import com.michaelrichards.laughlounge.repositories.TokenRepository
import com.michaelrichards.laughlounge.service.JWTService
import com.michaelrichards.laughlounge.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthenticationFilter(
    private val jwtService: JWTService,
    private val userService: UserService,
    private val tokenRepository: TokenRepository
): OncePerRequestFilter() {

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(AUTHORIZATION)
        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt: String = authHeader.substring(7)
        val username: String = jwtService.extractUsername(jwt)

        val token = tokenRepository.findByToken(jwt)
        if (token == null){
            filterChain.doFilter(request, response)
            return
        }

        if ((username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) && token.isTokenValid() ){
            val userDetails = userService.loadUserByUsername(username)
            if (jwtService.isTokenValid(jwt, userDetails)) run {
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                context.authentication = authToken
                SecurityContextHolder.setContext(context)
            }
        }
        filterChain.doFilter(request, response)
    }
}