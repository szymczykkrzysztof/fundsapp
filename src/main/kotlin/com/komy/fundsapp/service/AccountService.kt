package com.komy.fundsapp.service

import com.komy.fundsapp.models.entity.Account
import com.komy.fundsapp.models.enum.AccountType
import com.komy.fundsapp.repository.AccountRepository
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class AccountService
    (
    val accountRepository: AccountRepository,
    val userService: UserService
) {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional(readOnly = true)
    fun findAccountById(id: Long): Optional<Account> {
        return accountRepository.findById(id)
    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional(readOnly = true)
    fun getAccounts(userId: Long): List<Account> {
        return accountRepository.findByUserId(userId)
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    fun saveAccount(account: Account): Account {
        return accountRepository.save(account)
    }

    @Transactional
    fun saveMultipleAccounts(accounts: List<Account>): List<Account> {
        return accountRepository.saveAll(accounts)
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    fun deleteAccount(account: Account): Unit {
        accountRepository.delete(account)
    }

    @Transactional
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