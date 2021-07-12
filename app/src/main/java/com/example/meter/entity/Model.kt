package com.example.meter.entity

data class Model(
    val Count: Int,
    val Message: String,
    val Results: List<MetaData>,
    val SearchCriteria: String
)