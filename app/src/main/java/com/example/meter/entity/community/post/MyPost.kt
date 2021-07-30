package com.example.meter.entity.community.post


data class MyPost(
    val commentsAmount: Int?,
    val description: String?,
    val id: Int?,
    val likeAmount: Int?,
    val likedUserIds: List<String?>?,
    val photoCarUrl: List<String>,
    val title: String?,
    val user: User?,
    val views: Int?
) {
    data class User(
        val email: String?,
        val id: String?,
        val name: String?,
        val number: String?,
        val verified: Boolean?
    )
}