package com.michaelrichards.laughlounge.model

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID

@Entity
class mUser (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @field:Size(min = 5, message = "First name has to be at least 2 characters long")
    @field:Size(max = 30, message = "First name has to be 30 characters or less")
    @Column(nullable = false)
    var firstName: String,

    @field:Size(min = 5, message = "Last name has to be at least 2 characters long")
    @field:Size(max = 30, message = "Last name has to be 30 characters or less")
    @Column(nullable = false)
    var lastName: String,

    @field:Size(min = 5, message = "Username has to be at least 5 characters long")
    @field:Size(max = 30, message = "Username has to be 30 characters or less")
    @Column(unique = true)
    var username: String,


    @Column(unique = true)
    @field:Email(message = "Enter proper email")
    var email: String,

    var password: String,

    @Column(nullable = false)
    @field:Past(message = "birthday cannot be in the future")
    var birthday: LocalDate,


){
    @OneToMany(mappedBy = "mUser", cascade = [CascadeType.ALL], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf()


    companion object{
        fun mapToDto(user: mUser): UserDetailsResponse{
            return UserDetailsResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                username = user.username,
                birthday = user.birthday
            )
        }
    }

}