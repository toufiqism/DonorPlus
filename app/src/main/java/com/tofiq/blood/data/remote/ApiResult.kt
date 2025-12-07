package com.tofiq.blood.data.remote

import com.tofiq.blood.util.ExceptionHandler

/**
 * Sealed class representing the result of an API call.
 * 
 * This provides a type-safe way to handle different states of an API response:
 * - Loading: Request is in progress
 * - Success: Request completed successfully with data
 * - Empty: Request completed successfully but returned no data
 * - Error: Request failed with an error
 * 
 * Uses ExceptionHandler for consistent error message formatting across the app.
 * 
 * Usage example:
 * ```
 * safeApiFlow { apiService.getData() }.collect { result ->
 *     when (result) {
 *         is ApiResult.Loading -> showLoading()
 *         is ApiResult.Success -> handleData(result.data)
 *         is ApiResult.Empty -> showEmptyState()
 *         is ApiResult.Error -> showError(result.getUserMessage())
 *     }
 * }
 * ```
 */
sealed class ApiResult<out T> {
    
    /**
     * Represents a loading state while the API call is in progress.
     */
    data object Loading : ApiResult<Nothing>()
    
    /**
     * Represents a successful API response with data.
     * 
     * @param data The response body from the API
     */
    data class Success<T>(val data: T) : ApiResult<T>()
    
    /**
     * Represents a successful API response with no data (null body).
     */
    data object Empty : ApiResult<Nothing>()
    
    /**
     * Represents an error from the API call.
     * 
     * @param message Error message (from error body or exception)
     * @param statusCode HTTP status code (null for non-HTTP errors)
     * @param cause The underlying exception that caused the error
     */
    data class Error(
        val message: String? = null,
        val statusCode: Int? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>() {
        
        /**
         * Returns a user-friendly error message using ExceptionHandler.
         * Prioritizes: cause exception -> HTTP status code -> raw message -> default
         */
        fun getUserMessage(): String {
            // If we have a cause exception, use ExceptionHandler for consistent messaging
            cause?.let { 
                return ExceptionHandler.getErrorMessage(it, message ?: "An error occurred")
            }
            
            // If we have an HTTP status code, get appropriate message
            statusCode?.let { code ->
                return getHttpErrorMessage(code)
            }
            
            // Fall back to message or default
            return message ?: "An unknown error occurred"
        }
        
        /**
         * Returns HTTP status code specific error message.
         * Uses message from server if available, otherwise provides default.
         */
        private fun getHttpErrorMessage(code: Int): String {
            return when (code) {
                400 -> message ?: "Invalid request. Please check your input."
                401 -> message ?: "Invalid credentials. Please check your phone number and password."
                403 -> message ?: "Access denied. You don't have permission to perform this action."
                404 -> message ?: "Service not found. Please try again later."
                408 -> "Request timed out. Please try again."
                409 -> message ?: "This account already exists. Please try logging in."
                422 -> message ?: "Invalid data provided. Please check your input."
                429 -> "Too many requests. Please wait a moment and try again."
                in 500..599 -> "Server error. Please try again later."
                else -> message ?: "Request failed (Error $code). Please try again."
            }
        }
        
        /**
         * Checks if this error is a network-related error.
         */
        fun isNetworkError(): Boolean {
            return cause?.let { ExceptionHandler.isNetworkError(it) } 
                ?: (statusCode in 500..599)
        }
        
        /**
         * Checks if this error is an authentication error.
         */
        fun isAuthError(): Boolean {
            return cause?.let { ExceptionHandler.isAuthError(it) }
                ?: (statusCode in listOf(401, 403))
        }
    }
    
    /**
     * Returns true if this result is a success (Success or Empty).
     */
    val isSuccess: Boolean
        get() = this is Success || this is Empty
    
    /**
     * Returns true if this result is an error.
     */
    val isError: Boolean
        get() = this is Error
    
    /**
     * Returns true if this result is loading.
     */
    val isLoading: Boolean
        get() = this is Loading
    
    /**
     * Returns the data if this is a Success, null otherwise.
     */
    fun getOrNull(): T? = (this as? Success)?.data
    
    /**
     * Returns the data if this is a Success, throws exception otherwise.
     */
    fun getOrThrow(): T {
        return when (this) {
            is Success -> data
            is Error -> throw cause ?: Exception(message ?: "Unknown error")
            is Empty -> throw NoSuchElementException("Result is empty")
            is Loading -> throw IllegalStateException("Result is still loading")
        }
    }
    
    /**
     * Transforms the data if this is a Success.
     */
    inline fun <R> map(transform: (T) -> R): ApiResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Empty -> Empty
            is Loading -> Loading
        }
    }
    
    /**
     * Executes the given block if this is a Success.
     */
    inline fun onSuccess(action: (T) -> Unit): ApiResult<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes the given block if this is an Error.
     */
    inline fun onError(action: (Error) -> Unit): ApiResult<T> {
        if (this is Error) action(this)
        return this
    }
    
    /**
     * Executes the given block if this is Loading.
     */
    inline fun onLoading(action: () -> Unit): ApiResult<T> {
        if (this is Loading) action()
        return this
    }
}
