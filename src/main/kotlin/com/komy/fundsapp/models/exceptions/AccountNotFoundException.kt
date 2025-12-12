package com.komy.fundsapp.models.exceptions

/*
 * Thrown when an account is not found in the database
 */
class AccountNotFoundException(message: String? = "Account not found") : RuntimeException(message)