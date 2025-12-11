package com.komy.fundsapp.models.dto

import java.beans.ConstructorProperties

data class LoginDTO
@ConstructorProperties("email", "password")
constructor(
    val email: String,
    val password: String
)
