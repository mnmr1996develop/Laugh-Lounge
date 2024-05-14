package com.michaelrichards.laughlounge.exceptions.authExceptions

import org.springframework.web.bind.annotation.ControllerAdvice


sealed class AuthExceptions(message: String) : Exception(message){
    class UnAuthorizedAction(message: String): AuthExceptions(message)

    class InvalidName(name: String): AuthExceptions(message = "Name: $name is invalid")

    class InvalidUserNameLength(username: String): AuthExceptions(message = "Username '$username' is not between 5 and 30 characters")

    class InvalidUserName(message: String): AuthExceptions(message = message)

    class UsernameTaken(username: String): AuthExceptions(message = "Username: $username is already taken")

    class EmailTaken(email: String): AuthExceptions(message = "E-mail: $email is already taken")

    class InvalidAge(age: Int): AuthExceptions(message = if (age < 13) "$age is too young" else "$age is invalid")

    class InvalidPassword(message: String): AuthExceptions(message = message)
}