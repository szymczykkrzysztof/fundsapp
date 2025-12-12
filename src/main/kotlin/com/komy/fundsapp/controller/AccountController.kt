package com.komy.fundsapp.controller

import com.komy.fundsapp.models.UserSecurity
import com.komy.fundsapp.models.entity.Account
import com.komy.fundsapp.models.enum.AccountType
import com.komy.fundsapp.service.AccountService
import com.komy.fundsapp.service.UserService
import com.komy.fundsapp.utility.JWTUtility
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService,
    private val jwtUtility: JWTUtility,
    val userService: UserService
) {
    @PostMapping("/saving")
    fun createSavingAccount(@RequestHeader("Authorization") bearerToken: String) {
        val userId = jwtUtility.getUserId(bearerToken.substring(7))
        try {
            val newSavingAccount: Account = accountService.createAccount(userId, AccountType.SAVING)
            ResponseEntity<Account>(newSavingAccount, HttpStatus.CREATED)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    @PostMapping("/checking")
    fun createCheckingAccount(@AuthenticationPrincipal userDetails: UserSecurity) {
        try {
            val newCheckingAccount: Account = accountService.createAccount(userDetails.id, AccountType.CHECKING)
            ResponseEntity<Account>(newCheckingAccount, HttpStatus.CREATED)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    @GetMapping
    fun getAllAccounts(@AuthenticationPrincipal userDetails: UserSecurity): ResponseEntity<List<Account>> {
        val accounts = accountService.getAccounts(userDetails.id)
        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/{accountId}")
    fun getAccountById(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @PathVariable accountId: Long
    ): ResponseEntity<Account> {
        return accountService.findAccountById(accountId)
            .map { ResponseEntity.ok(it) }
            .orElseGet { ResponseEntity.status(HttpStatus.NOT_FOUND).build<Account>() }
    }

    @DeleteMapping("/{accountId}")
    fun deleteAccountById(
        @AuthenticationPrincipal userDetails: UserSecurity,
        @PathVariable accountId: Long
    ): ResponseEntity<Void> {
        val account = accountService.findAccountById(accountId)
        return if (account.isPresent) {
            accountService.deleteAccount(account.get())
            ResponseEntity.noContent().build<Void>()
        } else {
            ResponseEntity.notFound().build<Void>()
        }
    }
}