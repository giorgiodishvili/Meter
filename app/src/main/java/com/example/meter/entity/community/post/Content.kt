package com.example.meter.entity.community.post

import com.example.meter.entity.user.User

data class Content(
    val description: String,
    val id: Long,
    val photoCarUrl: List<String>,
    val title: String,
    val user: User,
    val views: Int,
    val likeAmount: Int,
    val commentsAmount: Int,
    val likedUserIds: MutableList<String>
)