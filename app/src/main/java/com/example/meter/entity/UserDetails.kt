package com.example.meter.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val name: String? = null,
    val number: String? = null,
    val email: String? = null,
    val id: String? = null,
    val verified: Boolean? = false,
    val url: String
): Parcelable {
}
