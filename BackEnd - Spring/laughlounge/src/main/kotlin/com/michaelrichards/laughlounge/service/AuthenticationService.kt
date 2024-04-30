package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.mUser
import com.michaelrichards.laughlounge.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class AuthenticationService(
    private val userRepository: UserRepository
) {


    fun registerUser(
        registrationRequest: RegistrationRequest
    ): UserDetailsResponse {

        val user = mUser(
            firstName = filterWhiteSpace(registrationRequest.firstName),
            lastName = filterWhiteSpace(registrationRequest.lastName),
            email = filterWhiteSpace(registrationRequest.email),
            username = filterWhiteSpace(registrationRequest.username),
            password = filterWhiteSpace(registrationRequest.password),
            birthday = registrationRequest.birthday
        )

        userRepository.save(user)

        return user.mapToDto()
    }


    private fun validateRegisterRequest(registrationRequest: RegistrationRequest){
            verifyAge(registrationRequest.birthday)
    }


    private fun verifyAge(birthday: LocalDate){
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())

        if (age < 13 || age > 150)
        {}
    }

    fun filterWhiteSpace(string: String) = string.filter { it.isWhitespace() }
}