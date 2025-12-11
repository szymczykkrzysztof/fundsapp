package com.komy.fundsapp.models.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SignUpDTO(
    @field:NotBlank(message = "First Name cannot be blank and must have greater length than 0")
    @field:NotEmpty(message = "First Name cannot be empty")
    var firstName: String?,
    @field:NotBlank(message = "Last Name cannot be blank and must have greater length than 0")
    @field:NotEmpty(message = "Last Name cannot be empty")
    var lastName: String?,
    @field:NotBlank(message = "Email cannot be blank and must have a valid email format")
    @field:NotEmpty(message = "Email cannot be empty")
    var email: String?,
    @field:NotBlank(message = "Password cannot be blank and must have greater length than 0")
    @field:NotEmpty(message = "Password cannot be empty")
    @field:Size(min = 4, max = 64, message = "Password must have at least 4 characters and max 64 characters")
    var password: String?,
    @field:NotNull(message = "Age cannot be null")
    @field:Min(16, message = "Age must be greater than 16")
    @field:Max(135, message = "Age must be less than 135")
    var age: Int
)
