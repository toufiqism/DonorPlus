package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Request model for user login
 */
data class LoginRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("password")
    val password: String
)

