package com.example.meter.entity

import com.example.meter.entity.community.post.Content
import com.example.meter.entity.community.post.User

data class Comment(
    val description: String,
    val id: Int,
    val post: Content,
    val user: User
)