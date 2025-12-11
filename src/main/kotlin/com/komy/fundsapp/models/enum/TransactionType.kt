package com.komy.fundsapp.models.enum

enum class TransactionType(val type: String) {
    DEPOSIT("DEPOSIT"),
    TRANSFER("TRANSFER"),
    WITHDRAW("WITHDRAW"),
    SEND("SEND")
}