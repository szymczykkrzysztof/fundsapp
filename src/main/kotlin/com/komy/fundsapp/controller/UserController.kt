package com.komy.fundsapp.controller

import com.komy.fundsapp.models.dto.SignUpDTO
import com.komy.fundsapp.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val userService: UserService
) {
    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signUpDTO: SignUpDTO): ResponseEntity<String> {
        return try {
            userService.signUpUser(signUpDTO)
            ResponseEntity("Created", HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.CONFLICT)
        }

    }
}