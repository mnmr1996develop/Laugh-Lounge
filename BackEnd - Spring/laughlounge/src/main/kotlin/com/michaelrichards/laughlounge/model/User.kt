package com.michaelrichards.laughlounge.model

import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "_user")
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val userId: UUID? = null,

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
    private var username: String,


    @Column(unique = true)
    @field:NotEmpty
    @field:Email(message = "Enter proper email")
    var email: String,

    private var password: String,

    @Column(nullable = false)
    @field:Past(message = "birthday cannot be in the future")
    var birthday: LocalDate,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "image_uuid")
    var profileImage: Image? = null,

    @OneToMany(mappedBy = "postAuthor", cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    val tokens: MutableSet<Token> = mutableSetOf(),

    @Enumerated(value = EnumType.STRING)
    var role: Role = Role.ROLE_USER,

    var accountCreatedAt: LocalDateTime,

    private var isAccountNonExpired: Boolean = true,

    private var isAccountNonLocked: Boolean = true,

    private var isCredentialsNonExpired: Boolean = true,

    private var isEnabled: Boolean = true,
): UserDetails {


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(
        SimpleGrantedAuthority(role.name)
    )

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username
    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean = this.isAccountNonExpired
    fun setIsAccountNonExpired(isAccountNonExpired: Boolean) {
        this.isAccountNonExpired = isAccountNonExpired
    }

    override fun isAccountNonLocked(): Boolean = this.isAccountNonLocked
    fun setIsAccountNonLocked(isAccountNonLocked: Boolean) {
        this.isAccountNonLocked = isAccountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean = this.isCredentialsNonExpired
    fun setIsCredentialsNonExpired(isCredentialsNonExpired: Boolean) {
        this.isCredentialsNonExpired = isCredentialsNonExpired
    }
    override fun isEnabled(): Boolean = this.isEnabled
    fun setIsEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }


    companion object{
        fun mapToDto(user: User, profileImageLink: String?): UserDetailsResponse{
            return UserDetailsResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                username = user.username,
                birthday = user.birthday,
                profileImageLink = profileImageLink
            )
        }
    }
}