package com.komy.fundsapp.models.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.komy.fundsapp.models.enum.TransactionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.Date

@Entity
@Table(name = "transactions")
@JsonInclude(JsonInclude.Include.NON_NULL)
class Transaction {
    @Id
    @GeneratedValue
    var id: Long? = null
    @Min(0)
    var amount: Double = 0.0

    var userId: Long = 0


    var fromAccountId: Long? = null
    var toAccountId: Long? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var transactionType: TransactionType? = null

    @CreationTimestamp
    val createdAt: Date? = null

    @UpdateTimestamp
    val updateAt: Date? = null
}