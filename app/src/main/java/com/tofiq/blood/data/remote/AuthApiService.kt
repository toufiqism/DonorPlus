package com.tofiq.blood.data.remote

import com.tofiq.blood.data.model.LoginRequest
import com.tofiq.blood.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service interface for authentication endpoints
 */
interface AuthApiService {
    
    /**
     * Login with phone number and password
     * @param loginRequest Contains phone number and password
     * @return LoginResponse with auth token and user info
     */
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}

