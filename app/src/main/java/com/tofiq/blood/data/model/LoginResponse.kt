package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response model for user login
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("statusCode")
    val statusCode: Int,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: LoginData?
)

/**
 * Data model containing login user information
 */
data class LoginData(
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("refreshToken")
    val refreshToken: String?,
    
    @SerializedName("userId")
    val userId: String?,
    
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)

