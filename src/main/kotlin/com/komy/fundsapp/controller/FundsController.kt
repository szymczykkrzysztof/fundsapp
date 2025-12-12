package com.komy.fundsapp.controller

import com.komy.fundsapp.models.UserSecurity
import com.komy.fundsapp.models.dto.FundsDTO
import com.komy.fundsapp.models.entity.Transaction
import com.komy.fundsapp.service.FundsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class FundsController(
    private val fundsService: FundsService
) {
    @GetMapping
    fun getTransactions(@AuthenticationPrincipal userDetails: UserSecurity): ResponseEntity<List<Transaction>> {
        val transactions = fundsService.getTransactions(userDetails.id)
        return ResponseEntity.ok(transactions)
    }

    @PostMapping("/deposit")
    fun handleDeposit(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @RequestBody @Valid fundsDTO: FundsDTO
    ): ResponseEntity<Transaction> {
        return try {
            val transaction = fundsService.handleDeposit(fundsDTO, userDetails.id)
            ResponseEntity.status(HttpStatus.CREATED).body(transaction)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/withdraw")
    fun handleWithdraw(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @RequestBody @Valid fundsDTO: FundsDTO
    ): ResponseEntity<Transaction> {
        return try {
            val transaction = fundsService.handleWithdraw(fundsDTO, userDetails.id)
            ResponseEntity.status(HttpStatus.CREATED).body(transaction)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/transfer")
    fun handleTransfer(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @RequestBody @Valid fundsDTO: FundsDTO
    ): ResponseEntity<Transaction> {
        return try {
            val transaction = fundsService.handleTransfer(fundsDTO, userDetails.id)
            ResponseEntity.status(HttpStatus.CREATED).body(transaction)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/send")
    fun handleBalance(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @RequestBody @Valid fundsDTO: FundsDTO
    ): ResponseEntity<Transaction> {
        return try {
            val transaction = fundsService.handleSend(fundsDTO, userDetails.id)
            ResponseEntity.status(HttpStatus.CREATED).body(transaction)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }


}