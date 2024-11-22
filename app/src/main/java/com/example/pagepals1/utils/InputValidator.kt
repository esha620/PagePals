package com.example.pagepals1.utils

object InputValidator {
    private val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$".toRegex()

    fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank()
    }
}
