package com.komy.fundsapp.config

import com.komy.fundsapp.utility.JWTUtility
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class SecurityConfig(
    private val jwtUtility: JWTUtility,
    private val userDetailsService: UserDetailsService
) {
    private fun authManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(userDetailsService)
        return authenticationManagerBuilder.build()
    }

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationManager = authManager(http)

        http
            .authenticationManager(authenticationManager)
            .addFilter(JwtAuthenticationFilter(jwtUtility, authenticationManager))
            .addFilter(JwtAuthorizationFilter(jwtUtility, userDetailsService, authenticationManager))
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/signup").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }

    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}