package com.komy.fundsapp.models.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.komy.fundsapp.models.enum.AccountType
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Check
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "accounts")
@Check(constraints = "balance >= 0")
class Account {
    @Id
    @GeneratedValue
    var id: Long? = null

    @NotBlank(message = "Name is mandatory")
    @NotEmpty(message = "Name cannot be empty")
    var name: String = ""

    @Min(0)
    @NotNull(message = "Balance cannot be null")
    var balance: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var accountType: AccountType? = null

    @CreationTimestamp
    val createdAt: Date? = null

    @UpdateTimestamp
    val updateAt: Date? = null

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    lateinit var user: User
}