package com.business.cmpproject.core.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure(val error: HttpError) : NetworkResult<Nothing>()
//    object Loading : NetworkResult<Nothing>()
}