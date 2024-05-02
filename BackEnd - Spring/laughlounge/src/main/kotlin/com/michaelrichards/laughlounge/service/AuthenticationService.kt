package com.michaelrichards.laughlounge.service

import com.michaelrichards.laughlounge.domain.request.RegistrationRequest
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.Image
import com.michaelrichards.laughlounge.model.mUser
import com.michaelrichards.laughlounge.repositories.UserRepository
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
import com.michaelrichards.laughlounge.utils.ImageUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val imageService: ImageService,
    private val environmentUtil: EnvironmentUtil
) {




    @Transactional
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

        val defaultProfileImage = Image(
            imageData = ImageUtils.compressImage(ImageUtils.createBasicProfileImage(user.firstName[0].uppercaseChar())),
            type = "image/png",
        )

        user.profileImage = imageService.saveImage(defaultProfileImage)
        userRepository.save(user)



        return mUser.mapToDto(user,  user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) })
    }


    private fun validateRegisterRequest(registrationRequest: RegistrationRequest){
            verifyAge(registrationRequest.birthday)
    }


    private fun verifyAge(birthday: LocalDate){
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())

        if (age < 13 || age > 150)
        {}
    }

    fun filterWhiteSpace(string: String) = string.filterNot { it.isWhitespace() }
}