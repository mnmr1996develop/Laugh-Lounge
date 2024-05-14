package com.michaelrichards.laughlounge.exceptions.authExceptions

import com.michaelrichards.laughlounge.domain.responses.APIException
import io.jsonwebtoken.security.InvalidKeyException
import jakarta.validation.ConstraintViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handleBadCredentialsException(exception: BadCredentialsException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "BAD_CREDENTIALS",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleWebExchangeBindException(e: ConstraintViolationException): ResponseEntity<APIException> {
        val httpStatus = HttpStatus.BAD_REQUEST

        val firstViolation = e.constraintViolations.firstOrNull()


        val apiException =
            firstViolation?.let {
                APIException(
                    httpStatus,
                    it.message,
                    "BAD_DATA",
                    LocalDateTime.now()
                )
            }

        return ResponseEntity.status(httpStatus).body(apiException)
    }

    @ExceptionHandler(value = [InvalidKeyException::class])
    fun handleInvalidToken(exception: InvalidKeyException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.UNAUTHORIZED
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "TOKEN_INVALID",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.NOT_FOUND
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "NOT_FOUND",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.EmailTaken::class])
    fun handleEmailTaken(exception: AuthExceptions.EmailTaken): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "EMAIL_TAKEN"

        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Email is taken",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.InvalidName::class])
    fun handleInvalidFirstName(exception: AuthExceptions.InvalidName): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INVALID_FIRST_NAME"

        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "first name is invalid",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.InvalidUserNameLength::class])
    fun handleInvalidUsernameLength(exception: AuthExceptions.InvalidUserNameLength): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INVALID_USERNAME"

        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                " Username must be  between 5 and 30 characters",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.InvalidUserName::class])
    fun handleInvalidUsername(exception: AuthExceptions.InvalidUserName): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INVALID_USERNAME"

        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Username Invalid",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.InvalidPassword::class])
    fun handleInvalidPassword(exception: AuthExceptions.InvalidPassword): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "PASSWORD_INVALID"

        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "password invalid",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.UsernameTaken::class])
    fun handleUsernameTaken(exception: AuthExceptions.UsernameTaken): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "USERNAME_TAKEN",
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Username is taken",
                "USERNAME_TAKEN",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthExceptions.InvalidAge::class])
    fun handleInvalidAge(exception: AuthExceptions.InvalidAge): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "INVALID_AGE",
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Age is invalid",
                "INVALID_AGE",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }
    
}