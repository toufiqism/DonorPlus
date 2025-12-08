package com.tofiq.blood.auth

import com.google.firebase.auth.FirebaseAuth
import com.tofiq.blood.data.local.PreferencesManager
import com.tofiq.blood.data.model.AuthToken
import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.LoginRequest
import com.tofiq.blood.data.model.RegisterRequest
import com.tofiq.blood.data.model.UserRole
import com.tofiq.blood.data.remote.ApiException
import com.tofiq.blood.data.remote.ApiResult
import com.tofiq.blood.data.remote.AuthApiService
import com.tofiq.blood.data.remote.safeApiCall
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    suspend fun loginWithPhonePassword(phoneNumber: String, password: String): Result<Unit>
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit>
    suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit>
    suspend fun register(
        phoneNumber: String,
        password: String,
        fullName: String,
        role: UserRole,
        agreedToTerms: Boolean,
        bloodGroup: BloodGroup,
        lastDonationDate: LocalDate? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<Unit>

    fun isLoggedIn(): Boolean
    fun signOut()
}

/**
 * REST API implementation of AuthRepository
 * Handles authentication using backend REST API with safe API layer
 */
@Singleton
class RestAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    /**
     * Login with phone number and password using REST API.
     * Uses safeApiCall for consistent error handling.
     */
    override suspend fun loginWithPhonePassword(
        phoneNumber: String,
        password: String
    ): Result<Unit> {
        val loginRequest = LoginRequest(
            phoneNumber = phoneNumber.trim(),
            password = password
        )

        // Use safe API call for consistent error handling
        return when (val result = safeApiCall { authApiService.login(loginRequest) }) {
            is ApiResult.Success -> {
                val loginResponse = result.data
                if (loginResponse.success && loginResponse.data?.refreshToken != null) {
                    // Success - save auth token
                    val authToken = AuthToken(
                        token = loginResponse.data.token ?: "",
                        refreshToken = loginResponse.data.refreshToken,
                        userId = loginResponse.data.userId,
                        phoneNumber = loginResponse.data.phoneNumber
                    )
                    preferencesManager.saveAuthToken(authToken)
                    Result.success(Unit)
                } else {
                    // API returned success=false
                    Result.failure(Exception(loginResponse.message ?: "Login failed"))
                }
            }

            is ApiResult.Empty -> {
                Result.failure(Exception("Login failed: No response received"))
            }

            is ApiResult.Error -> {
                val errorMessage = result.getUserMessage()
                Result.failure(
                    result.cause ?: ApiException(errorMessage, result.statusCode ?: 0)
                )
            }

            is ApiResult.Loading -> {
                // Should not happen with safeApiCall
                Result.failure(Exception("Unexpected loading state"))
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
     * Register a new user with REST API.
     * Uses safeApiCall for consistent error handling.
     */
    override suspend fun register(
        phoneNumber: String,
        password: String,
        fullName: String,
        role: UserRole,
        agreedToTerms: Boolean,
        bloodGroup: BloodGroup,
        lastDonationDate: LocalDate?,
        latitude: Double?,
        longitude: Double?
    ): Result<Unit> {
        val registerRequest = RegisterRequest(
            phoneNumber = phoneNumber.trim(),
            password = password,
            fullName = fullName.trim(),
            role = role,
            agreedToTerms = agreedToTerms,
            bloodGroup = bloodGroup,
            lastDonationDate = lastDonationDate?.toString(),
            latitude = latitude,
            longitude = longitude
        )

        // Use safe API call for consistent error handling
        return when (val result = safeApiCall { authApiService.register(registerRequest) }) {
            is ApiResult.Success -> {
                val registerResponse = result.data
                if (registerResponse.success && registerResponse.data?.accessToken != null) {
                    // Success - save auth token
                    val authToken = AuthToken(
                        token = registerResponse.data.accessToken,
                        refreshToken = registerResponse.data.refreshToken,
                        userId = registerResponse.data.userId,
                        phoneNumber = registerResponse.data.phoneNumber ?: phoneNumber.trim()
                    )
                    preferencesManager.saveAuthToken(authToken)
                    Result.success(Unit)
                } else {
                    // API returned success=false or missing token
                    Result.failure(Exception(registerResponse.message ?: "Registration failed"))
                }
            }

            is ApiResult.Empty -> {
                Result.failure(Exception("Registration failed: No response received"))
            }

            is ApiResult.Error -> {
                val errorMessage = result.getUserMessage()
                Result.failure(
                    result.cause ?: ApiException(errorMessage, result.statusCode ?: 0)
                )
            }

            is ApiResult.Loading -> {
                Result.failure(Exception("Unexpected loading state"))
            }
        }
    }

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

    override suspend fun loginWithPhonePassword(
        phoneNumber: String,
        password: String
    ): Result<Unit> =
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

    override suspend fun register(
        phoneNumber: String,
        password: String,
        fullName: String,
        role: UserRole,
        agreedToTerms: Boolean,
        bloodGroup: BloodGroup,
        lastDonationDate: LocalDate?,
        latitude: Double?,
        longitude: Double?
    ): Result<Unit> =
        Result.failure(UnsupportedOperationException("Registration with REST API fields not supported with Firebase"))

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun signOut() = firebaseAuth.signOut()
}