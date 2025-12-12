package com.komy.fundsapp.service

import com.komy.fundsapp.models.UserSecurity
import com.komy.fundsapp.models.exceptions.UserNotFoundException
import com.komy.fundsapp.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class UserDetailService(
    private val userRepository: UserRepository,

    ) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails? {
        val user = userRepository.findByEmail(email).orElseThrow { UserNotFoundException("User is not found") }
        user.id?.let {
            return UserSecurity(
                it,
                user.email,
                user.password,
                Collections.singleton(SimpleGrantedAuthority("user"))//Place for storing roles
            )
        }
        throw UserNotFoundException("User is not found")
    }
}