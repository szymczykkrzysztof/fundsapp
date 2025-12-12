package com.komy.fundsapp.service

import com.komy.fundsapp.models.dto.SignUpDTO
import com.komy.fundsapp.models.entity.Account
import com.komy.fundsapp.models.entity.User
import com.komy.fundsapp.models.enum.AccountType
import com.komy.fundsapp.repository.AccountRepository
import com.komy.fundsapp.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    val userRepository: UserRepository,
    val accountRepository: AccountRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }

    @Transactional
    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun signUpUser(signUpDTO: SignUpDTO) {
        if (signUpDTO.email?.let { getUserByEmail(it).getOrNull() } == null) {
            val user = User()
            user.firstName = signUpDTO.firstName ?: throw Exception("First name is required")
            user.lastName = signUpDTO.lastName ?: throw Exception("Last name is required")
            user.email = signUpDTO.email ?: throw Exception("Email is required")
            user.age = signUpDTO.age
            user.password = bCryptPasswordEncoder.encode(signUpDTO.password)

            saveUser(user)

            val account = Account()
            account.name = "${user.firstName} ${AccountType.CHECKING.name} ${UUID.randomUUID()}}"
            account.balance = 0.0
            account.user = user
            account.accountType = AccountType.CHECKING
            accountRepository.save(account)
            user.defaultAccountId = account.id
            saveUser(user)
        } else {
            throw Exception("User already exists, please login")
        }
    }
}