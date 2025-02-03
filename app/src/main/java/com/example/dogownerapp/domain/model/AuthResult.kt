package com.example.dogownerapp.domain.model

open class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}