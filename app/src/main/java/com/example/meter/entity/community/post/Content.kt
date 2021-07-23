package com.example.meter.entity.community.post

data class Content(
    val description: String,
    val id: Int,
    val photoCarUrl: List<String>,
    val title: String,
    val user: User,
    val views: Int,
    val likeAmount: Int,
    val commentsAmount: Int,
    val likedUserIds: MutableList<String>
)