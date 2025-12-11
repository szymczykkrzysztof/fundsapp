package com.komy.fundsapp.utility

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JWTUtility(
    @Value("\${funds_app.jwt.secret}") private val jwtSecret: String,
    @Value("\${funds_app.jwt.expiration}") private val expirationMs: Long
) {
    companion object : KLogging()

    private val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    fun generateToken(userId: Long, email: String): String {
        val now = Date()
        val expirationDate = Date(now.time + expirationMs)
        return Jwts.builder()
            .subject(userId.toString())
            .claim("userId", userId)
            .claim("email", email)
            .expiration(expirationDate)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    private fun getClaims(token: String): Claims? {

        return try {
            Jwts.parser()
                .verifyWith(key)            // ustawienie klucza
                .build()
                .parseSignedClaims(token)   // poprawna metoda dla JWT z podpisem
                .payload
        } catch (e: JwtException) {
            logger.error { "Invalid JWT token: ${e.message}" }
            null
        } catch (e: IllegalArgumentException) {
            logger.error { "Illegal argument exception: ${e.message}" }
            null
        } catch (e: Exception) {
            logger.error { "Exception occured: ${e.message}" }
            null
        }
    }

    fun getUserId(token: String): Long? = getClaims(token)?.get("userId") as Long

    fun getEmail(token: String): String = getClaims(token)?.get("email") as String

    fun isTokenValid(token: String): Boolean {
        val claims = getClaims(token)
        return if (claims != null) {
            val expirationDate = claims.expiration
            val now = Date(System.currentTimeMillis())
            now.before(expirationDate)
        } else {
            false
        }
    }
}