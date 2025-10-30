package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Request model for user registration
 * Matches backend RegisterRequest model structure
 */
data class RegisterRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("fullName")
    val fullName: String,
    
    @SerializedName("role")
    val role: UserRole,
    
    @SerializedName("agreedToTerms")
    val agreedToTerms: Boolean,
    
    @SerializedName("bloodGroup")
    val bloodGroup: BloodGroup,
    
    @SerializedName("lastDonationDate")
    val lastDonationDate: String? = null, // ISO date string format: "2025-01-20"
    
    @SerializedName("latitude")
    val latitude: Double? = null,
    
    @SerializedName("longitude")
    val longitude: Double? = null
)

