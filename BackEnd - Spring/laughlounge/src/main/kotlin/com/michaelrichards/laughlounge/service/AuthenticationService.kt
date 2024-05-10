package com.michaelrichards.laughlounge.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.michaelrichards.laughlounge.domain.request.AuthenticationRequest
import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.AuthenticationResponse
import com.michaelrichards.laughlounge.model.*
import com.michaelrichards.laughlounge.model.user.Role
import com.michaelrichards.laughlounge.model.user.Token
import com.michaelrichards.laughlounge.model.user.TrackedUserData
import com.michaelrichards.laughlounge.model.user.User
import com.michaelrichards.laughlounge.repositories.TokenRepository
import com.michaelrichards.laughlounge.repositories.UserRepository
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import com.michaelrichards.laughlounge.utils.ImageUtils
import com.michaelrichards.laughlounge.utils.Variables
import io.jsonwebtoken.security.InvalidKeyException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val imageService: ImageService,
    private val environmentUtil: EnvironmentUtil,
    private val tokenRepository: TokenRepository,
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JWTService,
    private val passwordEncoder: PasswordEncoder
) {




    @Transactional
    fun registerUser(
        registrationRequest: RegistrationRequest,
        role: Role = Role.ROLE_USER
    ): AuthenticationResponse {


        validateRegisterRequest(registrationRequest)
        val user = User(
            firstName = filterWhiteSpace(registrationRequest.firstName),
            lastName = filterWhiteSpace(registrationRequest.lastName),
            email = filterWhiteSpace(registrationRequest.email),
            username = filterWhiteSpace(registrationRequest.username),
            password = passwordEncoder.encode(filterWhiteSpace(registrationRequest.password)),
            birthday = registrationRequest.birthday,
            isProfilePublic = registrationRequest.isProfilePublic,
            accountCreatedAt = LocalDateTime.now()
        )

        val trackedUserData = TrackedUserData(
            lastRequest = LocalDateTime.now(),
            amountOfAPIRequest = 1,
            user = user
        )

        user.trackedUserData = trackedUserData

        val defaultProfileImage = Image(
            imageData = ImageUtils.compressImage(ImageUtils.createBasicProfileImage(user.firstName[0].uppercaseChar())),
            type = "image/png",
        )

        user.profileImage = imageService.saveImage(defaultProfileImage)
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(userDetails = user)
        val refreshToken = jwtService.generateRefreshToken(userDetails = user)
        saveUserToken(user = user, jwtToken)


        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
    }


    private fun validateRegisterRequest(registrationRequest: RegistrationRequest){
            verifyAge(registrationRequest.birthday)
    }


    private fun verifyAge(birthday: LocalDate){
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())

        if (age < 13 || age > 150)
        {}
    }

    fun login(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = userRepository.findByUsernameIgnoreCase(authenticationRequest.username)
            ?: throw UsernameNotFoundException("${authenticationRequest.username} Invalid username")

        user.trackedUserData?.let {
            it.amountOfAPIRequest += 1
        }
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(userDetails = user)
        val refreshToken = jwtService.generateRefreshToken(user)
        //revokeAllTokens(user)
        saveUserToken(user, jwtToken)

        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
    }

    private fun revokeAllTokens(user: User){
        val validUserTokens = user.userId?.let { tokenRepository.findAllValidTokensByUser(it) }
        if (validUserTokens != null) {
            if (validUserTokens.isEmpty()) return
            validUserTokens.forEach { token ->
                token.isRevoked = true
                token.isExpired = true
            }
            tokenRepository.saveAll(validUserTokens)
        }
    }

    private fun saveUserToken(user: User, token: String){

        val jwtToken = Token(
            user = user,
            token = token,
            isExpired = false,
            isRevoked = false
        )
        tokenRepository.save(jwtToken)
    }


    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(Variables.BEARER)) {
            return
        }
        val refreshToken: String = authHeader.substring(7)
        val username: String = jwtService.extractUsername(refreshToken)


        if (username.isNotEmpty()){
            val userDetails = userRepository.findByUsernameIgnoreCase(username) ?: throw UsernameNotFoundException("$username not found")
            if (jwtService.isTokenValid(refreshToken, userDetails)) run {
                val accessToken = jwtService.generateToken(userDetails)
                revokeAllTokens(userDetails)
                saveUserToken(userDetails, accessToken)
                val authResponse = AuthenticationResponse(
                    accessToken, refreshToken
                )

                ObjectMapper().writeValue(response.outputStream, authResponse )
            }
        }
    }



    fun authenticateToken(jwtToken: String){

        if (!tokenService.findToken(jwtToken).isTokenValid()) throw InvalidKeyException("Token not valid")
    }

    fun filterWhiteSpace(string: String) = string.filterNot { it.isWhitespace() }
}