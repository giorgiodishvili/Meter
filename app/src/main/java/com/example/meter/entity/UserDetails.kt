package com.example.meter.entity


data class UserDetails(
    val name: String? = null,
    val number: String? = null,
    val email: String? = null,
    val verified: Boolean? = false,
    val url: String
) {
}
