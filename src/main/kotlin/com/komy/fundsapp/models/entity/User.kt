package com.komy.fundsapp.models.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.Date

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue
    var id: Long? = null

    @NotBlank(message = "First Name is mandatory")
    @NotEmpty(message = "First Name cannot be empty")
    var firstName: String = ""

    @NotBlank(message = "Last Name is mandatory")
    @NotEmpty(message = "Last Name cannot be empty")
    var lastName: String = ""

    @NotBlank(message = "Email is mandatory")
    @NotEmpty(message = "Email cannot be empty")
    var email: String = ""

    @NotBlank(message = "Password is mandatory")
    @NotEmpty(message = "Password cannot be empty")
    var password: String = ""

    @Min(16, message = "Age must be greater than 16")
    @Max(135, message = "Age must be less than 135")
    var age: Int = 0

    var defaultAccountId: Long? = null

    @CreationTimestamp
    val createdAt: Date? = null

    @UpdateTimestamp
    val updateAt: Date? = null

    @OneToMany(mappedBy = "user")
    var accounts: MutableSet<Account>? = mutableSetOf()

}