package com.komy.fundsapp.repository

import com.komy.fundsapp.models.entity.Transaction
import com.komy.fundsapp.models.enum.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByUserId(userId: Long): List<Transaction>

//    @Query(value = "SELECT * FROM transactions t WHERE t.user_id = ?1", nativeQuery = true)
//    fun findByUserIdQuery(userId: Long): List<Transaction?>

    fun findByUserIdAndTransactionType(userId: Long, transactionType: TransactionType): List<Transaction?>
}