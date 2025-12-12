package com.komy.fundsapp.models.exceptions

/*
 * Thrown when a user already exist in the database
 */
class UserAlreadyExistException(message: String? = "User already exist") : RuntimeException(message)