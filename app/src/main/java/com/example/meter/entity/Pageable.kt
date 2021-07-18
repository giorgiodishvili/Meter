package com.example.meter.entity

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: Sort
)