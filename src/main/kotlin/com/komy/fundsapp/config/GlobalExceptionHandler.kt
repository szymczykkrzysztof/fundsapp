package com.komy.fundsapp.config

import com.komy.fundsapp.models.exceptions.*
import com.komy.fundsapp.models.response.ErrorResponse
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val genericErrorMessage = "An unexpected error occurred, try again or please contact support."
        return ResponseEntity(ErrorResponse.fromMessage(status, e.message ?: genericErrorMessage), status)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNPROCESSABLE_ENTITY
        return ResponseEntity(
            ErrorResponse.fromMessage(status, e.message ?: "User not found"), status
        )
    }

    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExistException(e: UserAlreadyExistException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT

        return ResponseEntity(ErrorResponse.fromMessage(status, e.message ?: "User already exist"), status)
    }

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(e: AccountNotFoundException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNPROCESSABLE_ENTITY
        return ResponseEntity(
            ErrorResponse.fromMessage(status, e.message ?: "Account not found"), status
        )
    }

    @ExceptionHandler(InsufficientFundsException::class)
    fun handleInsufficientFundsException(e: InsufficientFundsException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNPROCESSABLE_ENTITY
        return ResponseEntity(
            ErrorResponse.fromMessage(status, e.message ?: "Insufficient funds to complete the transaction"), status
        )
    }

    @ExceptionHandler(RewardServiceException::class)
    fun handleRewardServiceException(e: RewardServiceException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity(
            ErrorResponse.fromMessage(status, e.message ?: "Unable to process Reward Service Request"), status
        )
    }

    val pessimisticLockingFailureErrorMsg = "Unable to process request at this time, please try again later."

    @ExceptionHandler(PessimisticLockingFailureException::class)
    fun handlePessimisticLockingFailure(e: PessimisticLockingFailureException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT
        return ResponseEntity(ErrorResponse.fromMessage(status, pessimisticLockingFailureErrorMsg), status)
    }


    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST

        val errors = ex.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .associate { it.field to it.defaultMessage }

        return ResponseEntity(ErrorResponse.fromErrors(httpStatus, errors), httpStatus)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        return ResponseEntity(ErrorResponse.fromMessage(status, e.message ?: "Invalid request"), status)
    }
}
