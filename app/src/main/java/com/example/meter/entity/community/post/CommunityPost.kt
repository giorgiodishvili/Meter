package com.example.meter.entity.community.post

import com.example.meter.entity.Pageable
import com.example.meter.entity.Sort

data class CommunityPost(
    val content: List<Content>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort,
    val totalElements: Int,
    val totalPages: Int
)