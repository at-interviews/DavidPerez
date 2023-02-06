package com.dbperez.alltrailslunch.common

sealed class Resource<T>(val data: T? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(val message: String = "", data: T? = null) : Resource<T>(null)
}
