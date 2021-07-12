package com.example.meter.network

data class Resource<T>(val status: Status, val data: T? = null, val message: String? = null) {

    enum class Status { SUCCESS, ERROR, LOADING }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(Status.ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }
    }
}