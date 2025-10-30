package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response model for user registration
 */
data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("statusCode")
    val statusCode: Int,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: RegisterData?
)

/**
 * Data model containing registration user information
 */
data class RegisterData(
    @SerializedName("accessToken")
    val accessToken: String?,
    
    @SerializedName("refreshToken")
    val refreshToken: String?,
    
    @SerializedName("userId")
    val userId: String?,
    
    @SerializedName("fullName")
    val fullName: String?,
    
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)

