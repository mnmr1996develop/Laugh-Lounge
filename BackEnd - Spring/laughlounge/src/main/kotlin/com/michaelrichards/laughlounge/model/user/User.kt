package com.michaelrichards.laughlounge.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.michaelrichards.laughlounge.domain.responses.UserDetailsResponse
import com.michaelrichards.laughlounge.model.Image
import com.michaelrichards.laughlounge.model.following.BlockedUsers
import com.michaelrichards.laughlounge.model.following.FollowRelations
import com.michaelrichards.laughlounge.model.following.FollowRequest
import com.michaelrichards.laughlounge.model.posts.Post
import com.michaelrichards.laughlounge.utils.EnvironmentUtil
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


@Entity
@Table(name = "_user")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val userId: Long? = null,

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

    @field:JsonIgnore
    private var password: String,

    @Column(nullable = false)
    var isProfilePublic: Boolean,

    @Column(nullable = false)
    @field:Past(message = "birthday cannot be in the future")
    var birthday: LocalDate,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "image_uuid")
    @field:JsonIgnore
    var profileImage: Image? = null,


    @OneToMany(mappedBy = "postAuthor", cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: MutableList<Post> = mutableListOf(),


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    val tokens: MutableSet<Token> = mutableSetOf(),

    @field:JsonIgnore
    @Enumerated(value = EnumType.STRING)
    var role: Role = Role.ROLE_USER,

    var accountCreatedAt: LocalDateTime,

    private var isAccountNonExpired: Boolean = true,

    private var isAccountNonLocked: Boolean = true,

    private var isCredentialsNonExpired: Boolean = true,

    private var isEnabled: Boolean = true,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], optional = false, orphanRemoval = true)
    var trackedUserData: TrackedUserData? = null,


    ) : UserDetails {

    @Column
    @JsonIgnore
    @OneToMany(mappedBy = "follower", orphanRemoval = true, fetch = FetchType.EAGER)
    var following: MutableList<FollowRelations> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "following", orphanRemoval = true, fetch = FetchType.EAGER)
    var followers: MutableList<FollowRelations> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "followRequester", orphanRemoval = true)
    var followRequests: MutableList<FollowRequest> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "followRequester", orphanRemoval = true)
    var followRequestsSent: MutableList<FollowRequest> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "userBlocked", orphanRemoval = true)
    var usersBlockedMe: MutableList<BlockedUsers> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "blockedBy", orphanRemoval = true)
    var blockedUsers: MutableList<BlockedUsers> = mutableListOf()

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (birthday != other.birthday) return false
        if (role != other.role) return false
        if (accountCreatedAt != other.accountCreatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId?.hashCode() ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + accountCreatedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(userId=$userId, firstName='$firstName', lastName='$lastName', username='$username', email='$email', password='$password', isProfilePublic=$isProfilePublic, birthday=$birthday, isAccountNonExpired=$isAccountNonExpired, isAccountNonLocked=$isAccountNonLocked, isCredentialsNonExpired=$isCredentialsNonExpired, isEnabled=$isEnabled, role=$role)"
    }


    companion object {
        fun mapToDto(user: User, environmentUtil: EnvironmentUtil, isFollowingCaller: Boolean, callerIsFollowing: Boolean): UserDetailsResponse {
            return UserDetailsResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                username = user.username,
                birthday = user.birthday,
                isProfilePublic = user.isProfilePublic,
                profileImageLink = user.profileImage?.uuid?.let { environmentUtil.buildImageLink(it) },
                isFollowingCaller = isFollowingCaller,
                callerIsFollowing = callerIsFollowing
            )
        }
    }


}