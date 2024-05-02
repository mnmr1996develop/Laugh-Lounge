package com.michaelrichards.laughlounge.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JWTService(
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String,

    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration: Long,

    @Value("\${application.security.jwt.refresh.expiration}")
    private val jwtRefreshExpiration: Long,
) {



    fun extractUsername(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    private fun <T> extractClaim(token: String, claimsResolvers: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers.apply(claims)
    }

    fun generateToken(userDetails: UserDetails, extraClaims: Map<String, Any> = mutableMapOf()): String =
        buildToken(extraClaims, userDetails, jwtExpiration)

    fun generateRefreshToken(userDetails: UserDetails): String =
        buildToken(mutableMapOf(), userDetails, jwtRefreshExpiration)


    private fun extractAllClaims(token: String): Claims = Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .body


    private fun buildToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long) = Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact()

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date = extractClaim(token) { obj: Claims -> obj.expiration }
}