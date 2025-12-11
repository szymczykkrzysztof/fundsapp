package com.komy.fundsapp.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserSecurity(
    val id: Long,
    val email: String,
    private val userPassword: String,
    private val userAuthorities: MutableCollection<GrantedAuthority>

) : UserDetails {
    override fun getAuthorities() = userAuthorities
    override fun getPassword() = userPassword
    override fun getUsername() = email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
