package com.komy.fundsapp.service

import com.komy.fundsapp.models.dto.FundsDTO
import com.komy.fundsapp.models.entity.Account
import com.komy.fundsapp.models.entity.Transaction
import com.komy.fundsapp.models.entity.User
import com.komy.fundsapp.models.enum.TransactionType
import com.komy.fundsapp.models.exceptions.AccountNotFoundException
import com.komy.fundsapp.models.exceptions.InsufficientFundsException
import com.komy.fundsapp.models.exceptions.UserNotFoundException
import com.komy.fundsapp.repository.TransactionRepository
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrElse

@Service
class FundsService(
    private val transactionRepository: TransactionRepository,
    private val userService: UserService,
    private val accountService: AccountService
) {
    private fun createTransaction(
        userId: Long,
        amount: Double,
        transactionType: TransactionType,
        toAccountId: Long? = null,
        fromAccountId: Long? = null
    ): Transaction {
        val transaction = Transaction()
        transaction.userId = userId
        transaction.amount = amount
        transaction.transactionType = transactionType
        transaction.toAccountId = toAccountId
        transaction.fromAccountId = fromAccountId

        return transaction
    }

    fun getTransactions(userId:Long):List<Transaction>{
        return transactionRepository.findByUserId(userId)
    }


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(isolation= Isolation.SERIALIZABLE)
    fun handleDeposit(fundsDTO: FundsDTO, userId: Long): Transaction {
        val user = userService.getUserById(userId).getOrElse { throw UserNotFoundException("User not found") }
        val account =
            accountService.findAccountById(fundsDTO.toAccountId)
                .getOrElse { throw AccountNotFoundException("Account not found for the user") }
        if (user.id != account.user.id) {
            throw AccountNotFoundException("Account does not match the user")
        }
        account.balance += fundsDTO.amount
        val newTransaction =
            createTransaction(userId, fundsDTO.amount, TransactionType.DEPOSIT, toAccountId = account.id)

        transactionRepository.save(newTransaction)
        accountService.saveAccount(account)

        return newTransaction
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun handleWithdraw(fundsDTO: FundsDTO, userId: Long): Transaction {
        val user = userService.getUserById(userId).getOrElse { throw UserNotFoundException("User not found") }
        val account =
            accountService.findAccountById(fundsDTO.fromAccountId)
                .getOrElse { throw AccountNotFoundException("Account not found for the user") }
        if (user.id != account.user.id) {
            throw AccountNotFoundException("Account does not match the user")
        }
        if (account.balance < fundsDTO.amount) {
            throw InsufficientFundsException("Insufficient funds in account")
        }
        account.balance -= fundsDTO.amount
        val newTransaction =
            createTransaction(userId, fundsDTO.amount, TransactionType.WITHDRAW, fromAccountId = account.id)

        transactionRepository.save(newTransaction)
        accountService.saveAccount(account)

        return newTransaction
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    fun handleTransfer(fundsDTO: FundsDTO, userId: Long): Transaction {
        val user = userService.getUserById(userId).getOrElse { throw UserNotFoundException("User not found") }
        val fromAccount =
            accountService.findAccountById(fundsDTO.fromAccountId)
                .getOrElse { throw AccountNotFoundException("Source Account is invalid") }
        val toAccount =
            accountService.findAccountById(fundsDTO.toAccountId)
                .getOrElse { throw AccountNotFoundException("Destination Account is invalid") }

        validateAccountsOwner(user, fromAccount, toAccount)

        if (fromAccount.balance < fundsDTO.amount) {
            throw InsufficientFundsException("Insufficient funds in source account")
        }

        fromAccount.balance -= fundsDTO.amount
        toAccount.balance += fundsDTO.amount

        val newTransaction = createTransaction(
            userId,
            fundsDTO.amount,
            TransactionType.TRANSFER,
            toAccountId = toAccount.id,
            fromAccountId = fromAccount.id
        )
        transactionRepository.save(newTransaction)
        accountService.saveMultipleAccounts(listOf(fromAccount, toAccount))

        return newTransaction
    }

    private fun validateAccountsOwner(user: User, fromAccount: Account, toAccount: Account) {
        if (user.id != fromAccount.user.id || user.id != toAccount.user.id) {
            throw AccountNotFoundException("One or both accounts do not belong to the user")
        }

    }
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    fun handleSend(fundsDTO: FundsDTO, userId: Long): Transaction {
        val user =
            userService.getUserById(userId).getOrElse { throw UserNotFoundException("User attempting to send does not exist") }
        val fromAccount =
            accountService.findAccountById(fundsDTO.fromAccountId)
                .getOrElse { throw AccountNotFoundException("Sender Account is invalid") }

        if (user.id != fromAccount.user.id) {
            throw AccountNotFoundException("Sender account does not belong to the user")
        }

        val toUser =
            userService.getUserByEmail(fundsDTO.receiverEmail).getOrElse { throw Exception("Recipient does not exist") }
        val toAccountId = toUser.defaultAccountId ?: throw AccountNotFoundException("Recipient must have a send account setup")

        val toAccount = accountService.findAccountById(toAccountId)
            .getOrElse { throw AccountNotFoundException("Recipient account does not exist") }

        if (fromAccount.balance < fundsDTO.amount) {
            throw InsufficientFundsException("Insufficient funds in source account")
        }

        fromAccount.balance -= fundsDTO.amount
        toAccount.balance += fundsDTO.amount

        val fromTransaction = createTransaction(
            userId,
            fundsDTO.amount,
            TransactionType.SEND,
            toAccountId = toAccount.id,
            fromAccountId = fromAccount.id
        )

        val toTransaction = toUser.id?.let {
            createTransaction(
                it,
                fundsDTO.amount,
                TransactionType.SEND,
                toAccountId = toAccount.id,
                fromAccountId = fromAccount.id
            )
        } ?: throw UserNotFoundException("Recipient does not exist")
        transactionRepository.saveAll(listOf(fromTransaction, toTransaction))
        accountService.saveMultipleAccounts(listOf(fromAccount, toAccount))

        return fromTransaction

    }
}