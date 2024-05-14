package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.exceptions.authExceptions.AuthExceptions
import com.michaelrichards.laughlounge.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class UserVerificationService(
    private val userRepository: UserRepository
) {

    fun validateRegisterRequest(registrationRequest: RegistrationRequest){
        verifyUsername(registrationRequest.username)
        verifyEmail(registrationRequest.email)
        verifyNames(listOf(registrationRequest.firstName, registrationRequest.lastName))
        verifyAge(registrationRequest.birthday)
    }


    private fun verifyNames(names: List<String>) {
        names.forEach { name ->
            if (name.isEmpty() || !name.onlyLetters() ) throw AuthExceptions.InvalidName(name)
        }
    }

    private fun verifyEmail(email: String) {
        if (userRepository.existsByEmailIgnoreCase(email)) throw AuthExceptions.EmailTaken(email)
    }


    private fun verifyUsername(username: String) {
        if (userRepository.existsByUsernameIgnoreCase(username)) throw AuthExceptions.UsernameTaken(username)
    }

    private fun verifyAge(birthday: LocalDate){
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())

        if (age < 13 || age > 150) throw AuthExceptions.InvalidAge(age.toInt())
    }

    private fun String.onlyLetters() = all { it.isLetter() }

}