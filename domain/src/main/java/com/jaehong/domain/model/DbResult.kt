package com.jaehong.domain.model

sealed class DbResult<out T : Any> {
    data class Success<out T : Any>(val data: T ): DbResult<T>()
    data class Error(val exception: Throwable) : DbResult<Nothing>()
}