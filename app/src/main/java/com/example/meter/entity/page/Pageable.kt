package com.example.meter.entity.page

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: Sort? = null
)