package com.example.meter.entity.user

data class User(
    val email: String,
    val id: String,
    val name: String,
    val number: String,
    val verified: Boolean,
    val url: String
)