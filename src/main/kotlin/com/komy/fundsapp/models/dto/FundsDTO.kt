package com.komy.fundsapp.models.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class FundsDTO(
    var receiverEmail: String = "",
    @field:NotNull(message = "Amount needs a valid value")
    @field:Min(0, message = "Amount cannot be negative")
    var amount: Double,
    @field:NotNull(message = "From Account Id needs a valid value")
    var fromAccountId: Long,
    var toAccountId: Long
)
