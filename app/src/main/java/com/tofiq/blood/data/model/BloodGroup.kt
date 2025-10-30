package com.tofiq.blood.data.model

import com.google.gson.annotations.SerializedName

/**
 * Enumeration for blood groups
 */
enum class BloodGroup {
    @SerializedName("O+")
    O_POSITIVE,

    @SerializedName("O-")
    O_NEGATIVE,

    @SerializedName("A+")
    A_POSITIVE,

    @SerializedName("A-")
    A_NEGATIVE,

    @SerializedName("B+")
    B_POSITIVE,

    @SerializedName("B-")
    B_NEGATIVE,

    @SerializedName("AB+")
    AB_POSITIVE,

    @SerializedName("AB-")
    AB_NEGATIVE
}

