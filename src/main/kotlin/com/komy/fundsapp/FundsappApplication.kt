package com.komy.fundsapp

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FundsappApplication

val dotenv = dotenv {
    ignoreIfMissing = true
}

fun main(args: Array<String>) {
    System.setProperty("DB_USERNAME", dotenv["DB_USERNAME"])
    System.setProperty("DB_PASSWORD", dotenv["DB_PASSWORD"])
    System.setProperty("DB_URL", dotenv["DB_URL"])
    System.setProperty("JWT_SECRET", dotenv["JWT_SECRET"])
    runApplication<FundsappApplication>(*args)
}
