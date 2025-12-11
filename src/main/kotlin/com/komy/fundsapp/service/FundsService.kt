package com.komy.fundsapp.service

import com.komy.fundsapp.models.dto.FundsDTO
import com.komy.fundsapp.models.entity.Transaction
import com.komy.fundsapp.models.enum.TransactionType
import com.komy.fundsapp.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class FundsService(val transactionRepository: TransactionRepository) {
    fun handleDeposit(fundsDTO: FundsDTO, userId: Long): Transaction {
        val transaction = Transaction()
        transaction.userId = userId
        transaction.amount = fundsDTO.amount
        transaction.transactionType = TransactionType.DEPOSIT

        transactionRepository.save(transaction)
        return transaction

    }

    fun handleWithdraw(fundsDTO: FundsDTO, userId: Long): Transaction {
        val transaction = Transaction()
        transaction.userId = userId
        transaction.amount = fundsDTO.amount
        transaction.transactionType = TransactionType.WITHDRAW

        transactionRepository.save(transaction)
        return transaction

    }

    fun handleTransfer(fundsDTO: FundsDTO, userId: Long): Transaction {
        val transaction = Transaction()
        transaction.userId = userId
        transaction.amount = fundsDTO.amount
        transaction.transactionType = TransactionType.TRANSFER

        transactionRepository.save(transaction)
        return transaction

    }

    fun handleSend(fundsDTO: FundsDTO, userId: Long): Transaction {
        val transaction = Transaction()
        transaction.userId = userId
        transaction.amount = fundsDTO.amount
        transaction.transactionType = TransactionType.SEND

        transactionRepository.save(transaction)
        return transaction

    }
}