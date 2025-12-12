package com.komy.fundsapp.models.exceptions

/*
 * Thrown when an account has insufficient funds for a transaction
 */
class InsufficientFundsException(message: String? = "Insufficient Funds") : RuntimeException(message)