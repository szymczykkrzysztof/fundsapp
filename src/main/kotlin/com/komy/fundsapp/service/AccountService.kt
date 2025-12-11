package com.komy.fundsapp.service

import com.komy.fundsapp.models.entity.Account
import com.komy.fundsapp.models.enum.AccountType
import com.komy.fundsapp.repository.AccountRepository
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class AccountService
    (
    val accountRepository: AccountRepository,
    val userService: UserService
) {
    fun saveAccount(account: Account): Account {
        return accountRepository.save(account)
    }

    fun deleteAccount(account: Account): Unit {
        accountRepository.delete(account)
    }

    fun findAccountById(id: Long): Optional<Account> {
        return accountRepository.findById(id)
    }

    fun createAccount(userId: Long, accountType: AccountType): Account {
        val user = userService.getUserById(userId).getOrElse { throw Exception("User not found") }
        val account = Account()
        account.name = "${user.firstName} ${accountType.name} ${UUID.randomUUID()}"
        account.balance = 0.0
        account.user = user
        account.accountType = accountType

        return saveAccount(account)
    }
}