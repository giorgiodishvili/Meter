package com.example.meter.entity.community.post

data class Content(
    val description: String,
    val id: Int,
    val photoCarUrl: List<String>,
    val user: User
)