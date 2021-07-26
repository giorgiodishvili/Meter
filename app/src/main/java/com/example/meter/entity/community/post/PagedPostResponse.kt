package com.example.meter.entity.community.post

import com.example.meter.entity.page.Pageable
import com.example.meter.entity.page.Sort

data class PagedPostResponse<T>(
    val content: List<T>,
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