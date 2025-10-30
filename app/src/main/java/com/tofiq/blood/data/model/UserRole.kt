package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Enumeration for user roles in the DonorPlus app
 */
enum class UserRole {
    @SerializedName("DONOR")
    DONOR,
    
    @SerializedName("REQUESTER")
    REQUESTER
}

