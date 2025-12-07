package com.tofiq.blood.data.remote

import com.tofiq.blood.util.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

/**
 * Safe API call utilities for handling network requests.
 * 
 * These functions provide a consistent way to handle API responses,
 * including error handling, loading states, and response parsing.
 * 
 * Integrates with ExceptionHandler for consistent error messaging across the app.
 */

/**
 * Executes an API call and emits results as a Flow.
 * 
 * This function:
 * 1. Emits Loading state immediately
 * 2. Executes the API call on IO dispatcher
 * 3. Emits Success/Empty/Error based on response
 * 4. Catches exceptions and emits Error state
 * 
 * @param apiCall Suspend function that returns a Retrofit Response
 * @return Flow of ApiResult states
 * 
 * Usage:
 * ```
 * safeApiFlow { apiService.login(request) }
 *     .collect { result ->
 *         when (result) {
 *             is ApiResult.Loading -> _uiState.update { it.copy(isLoading = true) }
 *             is ApiResult.Success -> handleSuccess(result.data)
 *             is ApiResult.Empty -> handleEmpty()
 *             is ApiResult.Error -> handleError(result.getUserMessage())
 *         }
 *     }
 * ```
 */
suspend fun <T> safeApiFlow(
    apiCall: suspend () -> Response<T>
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)
    
    try {
        val response = apiCall()
        
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(ApiResult.Success(body))
            } else {
                emit(ApiResult.Empty)
            }
        } else {
            // Parse error body for message
            val errorMessage = parseErrorBody(response.errorBody()?.string())
            emit(
                ApiResult.Error(
                    message = errorMessage,
                    statusCode = response.code()
                )
            )
        }
    } catch (e: Exception) {
        emit(
            ApiResult.Error(
                message = e.message,
                cause = e
            )
        )
    }
}.flowOn(Dispatchers.IO)

/**
 * Executes an API call and returns a single ApiResult.
 * 
 * Use this when you don't need the Loading state or Flow semantics.
 * 
 * @param apiCall Suspend function that returns a Retrofit Response
 * @return ApiResult with Success/Empty/Error
 * 
 * Usage:
 * ```
 * val result = safeApiCall { apiService.getData() }
 * when (result) {
 *     is ApiResult.Success -> handleData(result.data)
 *     is ApiResult.Error -> showError(result.getUserMessage())
 *     else -> { }
 * }
 * ```
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Empty
            }
        } else {
            val errorMessage = parseErrorBody(response.errorBody()?.string())
            ApiResult.Error(
                message = errorMessage,
                statusCode = response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(
            message = e.message,
            cause = e
        )
    }
}

/**
 * Executes an API call and returns a Kotlin Result.
 * 
 * Use this for compatibility with existing code that uses Result<T>.
 * Uses ExceptionHandler for consistent error messaging.
 * 
 * @param apiCall Suspend function that returns a Retrofit Response
 * @return Result with success value or failure exception
 */
suspend fun <T> safeApiResult(
    apiCall: suspend () -> Response<T>
): Result<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(NoSuchElementException("Response body is null"))
            }
        } else {
            val errorMessage = parseErrorBody(response.errorBody()?.string())
                ?: getHttpErrorMessage(response.code())
            Result.failure(ApiException(errorMessage, response.code()))
        }
    } catch (e: Exception) {
        // Use ExceptionHandler for user-friendly error message
        val userMessage = ExceptionHandler.getErrorMessage(e)
        Result.failure(ApiException(userMessage, 0, e))
    }
}

/**
 * Parses error body JSON to extract error message.
 * 
 * Attempts to parse common error response formats:
 * - { "message": "..." }
 * - { "error": "..." }
 * - { "error": { "message": "..." } }
 */
private fun parseErrorBody(errorBody: String?): String? {
    if (errorBody.isNullOrBlank()) return null
    
    return try {
        val json = JSONObject(errorBody)
        
        // Try common error message fields
        json.optString("message").takeIf { it.isNotBlank() }
            ?: json.optString("error").takeIf { it.isNotBlank() }
            ?: json.optJSONObject("error")?.optString("message")?.takeIf { it.isNotBlank() }
            ?: json.optString("detail").takeIf { it.isNotBlank() }
            ?: errorBody
    } catch (e: Exception) {
        errorBody
    }
}

/**
 * Returns a user-friendly error message for HTTP status codes.
 * Used when no error body message is available.
 */
private fun getHttpErrorMessage(code: Int): String {
    return when (code) {
        400 -> "Invalid request. Please check your input."
        401 -> "Invalid credentials. Please check your phone number and password."
        403 -> "Access denied. You don't have permission to perform this action."
        404 -> "Service not found. Please try again later."
        408 -> "Request timed out. Please try again."
        409 -> "This account already exists. Please try logging in."
        422 -> "Invalid data provided. Please check your input."
        429 -> "Too many requests. Please wait a moment and try again."
        in 500..599 -> "Server error. Please try again later."
        else -> "Request failed (Error $code). Please try again."
    }
}

/**
 * Custom exception for API errors with status code.
 * 
 * @param message User-friendly error message
 * @param statusCode HTTP status code (0 for non-HTTP errors)
 * @param cause The underlying exception that caused the error
 */
class ApiException(
    override val message: String,
    val statusCode: Int,
    cause: Throwable? = null
) : Exception(message, cause) {
    
    /**
     * Checks if this is a network-related error.
     */
    fun isNetworkError(): Boolean {
        return cause?.let { ExceptionHandler.isNetworkError(it) }
            ?: (statusCode in 500..599)
    }
    
    /**
     * Checks if this is an authentication error.
     */
    fun isAuthError(): Boolean {
        return cause?.let { ExceptionHandler.isAuthError(it) }
            ?: (statusCode in listOf(401, 403))
    }
}
