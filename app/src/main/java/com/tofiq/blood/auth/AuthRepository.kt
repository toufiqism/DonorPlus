package com.tofiq.blood.auth

import com.google.firebase.auth.FirebaseAuth
import com.tofiq.blood.data.local.PreferencesManager
import com.tofiq.blood.data.model.AuthToken
import com.tofiq.blood.data.model.LoginRequest
import com.tofiq.blood.data.remote.AuthApiService
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    suspend fun loginWithPhonePassword(phoneNumber: String, password: String): Result<Unit>
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit>
    suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
    fun signOut()
}

/**
 * REST API implementation of AuthRepository
 * Handles authentication using backend REST API
 */
@Singleton
class RestAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    /**
     * Login with phone number and password using REST API
     */
    override suspend fun loginWithPhonePassword(phoneNumber: String, password: String): Result<Unit> =
        runCatching {
            val loginRequest = LoginRequest(
                phoneNumber = phoneNumber.trim(),
                password = password
            )
            
            val response = authApiService.login(loginRequest)
            
            if (response.isSuccessful) {
                val loginResponse = response.body()
                
                // Check if the API call was successful
                if (loginResponse != null) {
                    if (loginResponse.success && loginResponse.data?.token != null) {
                        // Success - save auth token
                        val authToken = AuthToken(
                            token = loginResponse.data.token,
                            refreshToken = loginResponse.data.refreshToken,
                            userId = loginResponse.data.userId,
                            phoneNumber = loginResponse.data.phoneNumber
                        )
                        preferencesManager.saveAuthToken(authToken)
                        Unit
                    } else {
                        // API returned success=false
                        throw Exception(loginResponse.message ?: "Login failed")
                    }
                } else {
                    throw Exception("Login failed: No response received")
                }
            } else {
                // HTTP error - try to parse error body
                val errorResponse = response.errorBody()?.let { it ->
                    val errorJson = JSONObject(it.string())
                    val errorMessage = errorJson.optString("message")
                    val errorCode = errorJson.optString("code")
                    val error = Exception("Login failed: ${response.code()} - ${errorMessage ?: "Unknown error"}")
                    error.addSuppressed(Throwable("Error response: $it"))
                    throw error
                }
            }
        }

    /**
     * Login with email and password (for backward compatibility)
     * Not implemented for REST API
     */
    override suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit> =
        Result.failure(UnsupportedOperationException("Email login not supported with REST API"))

    /**
     * Register with email and password (for backward compatibility)
     * Not implemented for REST API
     */
    override suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit> =
        Result.failure(UnsupportedOperationException("Registration not supported with REST API"))

    /**
     * Check if user is logged in
     */
    override fun isLoggedIn(): Boolean = preferencesManager.isLoggedIn()

    /**
     * Sign out the current user
     */
    override fun signOut() {
        preferencesManager.clearAuthData()
    }
}

/**
 * Firebase implementation of AuthRepository (kept for backward compatibility)
 */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginWithPhonePassword(phoneNumber: String, password: String): Result<Unit> =
        Result.failure(UnsupportedOperationException("Phone login not supported with Firebase"))

    override suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            Unit
        }

    override suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            Unit
        }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun signOut() = firebaseAuth.signOut()
}