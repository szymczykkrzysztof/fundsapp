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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
            .csrf { it.disable() }
            .cors { }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationManager(authenticationManager)
            .addFilterBefore(
                JwtAuthorizationFilter(
                    jwtUtility, userDetailsService,
                    authManager = authenticationManager
                ),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilter(
                JwtAuthenticationFilter(jwtUtility, authenticationManager)
            )

            .authorizeHttpRequests {
                it.requestMatchers("/actuator/health").permitAll()
                it.requestMatchers("/signup", "/signin").permitAll()
                it.anyRequest().authenticated()
            }

        return http.build()
    }


    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}