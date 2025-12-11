package com.komy.fundsapp.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @PostMapping("/signup")
    fun signup(): String {
        return "Hello World\n"
    }
}