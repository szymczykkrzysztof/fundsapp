package com.komy.fundsapp.models.exceptions

/*
 * Thrown when a user is not found in the database
 */
class UserNotFoundException(message: String? = "User not found") : RuntimeException(message)