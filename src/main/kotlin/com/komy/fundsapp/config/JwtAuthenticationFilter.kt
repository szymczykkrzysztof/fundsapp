package com.komy.fundsapp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.komy.fundsapp.models.UserSecurity
import com.komy.fundsapp.models.dto.LoginDTO
import com.komy.fundsapp.models.response.TokenResponse
import com.komy.fundsapp.utility.JWTUtility
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class JwtAuthenticationFilter(
    private val jwtTokenUtil: JWTUtility,
    private val authManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        val credentials = ObjectMapper().readValue(request?.inputStream, LoginDTO::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.email,
            credentials.password,
            Collections.singleton(SimpleGrantedAuthority("user"))
        )

        return authManager.authenticate(auth)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val userId = (authResult?.principal as UserSecurity).id
        val email = (authResult.principal as UserSecurity).email
        val token = jwtTokenUtil.generateToken(userId, email)
        val tokenResponse = TokenResponse(token)
        val json = Gson().toJson(tokenResponse)
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.addHeader("Authorization", "Bearer $token")
        response?.writer?.print(json)
        response?.addHeader("Access-Control-Expose-Headers", "Authorization")
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        val error = InvalidCredentialsError()
        response?.status = error.status
        response?.contentType = "application/json"
        response?.writer?.append(error.toString())
    }

    private data class InvalidCredentialsError(
        val timestamp: String = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        val status: Int = 401,
        val message: String = "Please check your email and password"
    ) {
        override fun toString(): String {
            return ObjectMapper().writeValueAsString(this)
        }
    }
}