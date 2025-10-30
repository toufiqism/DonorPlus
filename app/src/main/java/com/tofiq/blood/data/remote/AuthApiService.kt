package com.tofiq.blood.data.remote

import com.tofiq.blood.data.model.LoginRequest
import com.tofiq.blood.data.model.LoginResponse
import com.tofiq.blood.data.model.RegisterRequest
import com.tofiq.blood.data.model.RegisterResponse
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
    
    /**
     * Register a new user
     * @param registerRequest Contains user registration information
     * @return RegisterResponse with auth token and user info
     */
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>
}

