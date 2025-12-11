package com.komy.fundsapp.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class FundsController {
    @GetMapping()
    fun getTransactions(): String {
        return "Get transactions"
    }

    @PostMapping("/deposit")
    fun handleDeposit(): String {
        return "Deposits"
    }

    @PostMapping("/withdraw")
    fun handleWithdraw(): String {
        return "Withdraws"
    }

    @PostMapping("/transfer")
    fun handleTransfer(): String {
        return "Transfer"
    }

    @PostMapping("/send")
    fun handleBalance(): String {
        return "Send"
    }


}