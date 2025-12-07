package com.tofiq.blood.util

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Utility object for handling exceptions and converting them to user-friendly messages.
 * 
 * Handles various exception types:
 * - Network/Connection errors (no internet, timeout, DNS failure)
 * - HTTP errors (server errors, client errors)
 * - Firebase authentication errors
 * - SSL/TLS errors
 * - Generic exceptions
 */
object ExceptionHandler {

    /**
     * Converts an exception to a user-friendly error message.
     * 
     * @param throwable The exception to handle
     * @param defaultMessage Fallback message if no specific handling exists
     * @return User-friendly error message
     */
    fun getErrorMessage(throwable: Throwable, defaultMessage: String = "An error occurred"): String {
        return when (throwable) {
            // Network connectivity issues
            is UnknownHostException -> "No internet connection. Please check your network settings."
            is ConnectException -> "Unable to connect to server. Please check your internet connection."
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is SSLException -> "Secure connection failed. Please try again later."
            
            // HTTP errors from Retrofit
            is HttpException -> handleHttpException(throwable)
            
            // Firebase specific errors
            is FirebaseNetworkException -> "Network error. Please check your internet connection."
            is FirebaseAuthException -> handleFirebaseAuthException(throwable)
            
            // Generic exceptions with messages
            else -> throwable.localizedMessage?.takeIf { it.isNotBlank() } ?: defaultMessage
        }
    }

    /**
     * Handles HTTP exceptions and returns appropriate messages based on status code.
     */
    private fun handleHttpException(exception: HttpException): String {
        return when (exception.code()) {
            400 -> "Invalid request. Please check your input."
            401 -> "Invalid credentials. Please check your phone number and password."
            403 -> "Access denied. You don't have permission to perform this action."
            404 -> "Service not found. Please try again later."
            408 -> "Request timed out. Please try again."
            409 -> "This account already exists. Please try logging in."
            422 -> "Invalid data provided. Please check your input."
            429 -> "Too many requests. Please wait a moment and try again."
            in 500..599 -> "Server error. Please try again later."
            else -> "Request failed (${exception.code()}). Please try again."
        }
    }

    /**
     * Handles Firebase authentication exceptions.
     */
    private fun handleFirebaseAuthException(exception: FirebaseAuthException): String {
        return when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> "Invalid email address format."
            "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again."
            "ERROR_USER_NOT_FOUND" -> "No account found with this email."
            "ERROR_USER_DISABLED" -> "This account has been disabled."
            "ERROR_TOO_MANY_REQUESTS" -> "Too many failed attempts. Please try again later."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered."
            "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please use a stronger password."
            "ERROR_OPERATION_NOT_ALLOWED" -> "This operation is not allowed."
            "ERROR_INVALID_CREDENTIAL" -> "Invalid credentials. Please try again."
            else -> exception.localizedMessage ?: "Authentication failed."
        }
    }

    /**
     * Checks if the exception is a network-related error.
     */
    fun isNetworkError(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is ConnectException ||
                throwable is SocketTimeoutException ||
                throwable is SSLException ||
                throwable is FirebaseNetworkException ||
                (throwable is HttpException && throwable.code() in 500..599)
    }

    /**
     * Checks if the exception is an authentication error.
     */
    fun isAuthError(throwable: Throwable): Boolean {
        return throwable is FirebaseAuthException ||
                (throwable is HttpException && throwable.code() in listOf(401, 403))
    }
}
