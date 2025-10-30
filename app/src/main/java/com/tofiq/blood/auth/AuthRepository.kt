package com.tofiq.blood.auth

import com.google.firebase.auth.FirebaseAuth
import com.tofiq.blood.data.local.PreferencesManager
import com.tofiq.blood.data.model.AuthToken
import com.tofiq.blood.data.model.LoginRequest
import com.tofiq.blood.data.model.RegisterRequest
import com.tofiq.blood.data.model.RegisterResponse
import com.tofiq.blood.data.remote.AuthApiService
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.UserRole
import java.time.LocalDate

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
     * Register a new user with REST API
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
    ): Result<Unit> = runCatching {
        val registerRequest = RegisterRequest(
            phoneNumber = phoneNumber.trim(),
            password = password,
            fullName = fullName.trim(),
            role = role,
            agreedToTerms = agreedToTerms,
            bloodGroup = bloodGroup,
            lastDonationDate = lastDonationDate?.toString(), // Convert LocalDate to ISO string
            latitude = latitude,
            longitude = longitude
        )

        val response = authApiService.register(registerRequest)

        if (response.isSuccessful) {
            val registerResponse = response.body()

            // Check if the API call was successful
            if (registerResponse != null) {
                if (registerResponse.success && registerResponse.data?.accessToken != null) {
                    // Success - save auth token
                    // Use phoneNumber from request if not provided in response
                    val authToken = AuthToken(
                        token = registerResponse.data.accessToken,
                        refreshToken = registerResponse.data.refreshToken,
                        userId = registerResponse.data.userId,
                        phoneNumber = registerResponse.data.phoneNumber ?: phoneNumber.trim()
                    )
                    preferencesManager.saveAuthToken(authToken)
                    Unit
                } else {
                    // API returned success=false or missing token
                    throw Exception(registerResponse.message ?: "Registration failed")
                }
            } else {
                throw Exception("Registration failed: No response received")
            }
        } else {
            // HTTP error - try to parse error body
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody?.let {
                try {
                    val errorJson = JSONObject(it)
                    errorJson.optString("message", "Unknown error")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            throw Exception("Registration failed: ${response.code()} - $errorMessage")
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