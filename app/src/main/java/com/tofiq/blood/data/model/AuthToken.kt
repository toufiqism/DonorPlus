package com.tofiq.blood.data.model

/**
 * Model to hold authentication token information
 */
data class AuthToken(
    val token: String,
    val refreshToken: String? = null,
    val userId: String? = null,
    val phoneNumber: String? = null
)

