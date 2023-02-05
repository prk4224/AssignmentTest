package com.jaehong.presentation.util

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.DbResult

fun <T : Any> checkedResult(
    dbResult: DbResult<T>? = null,
    apiResult: ApiResult<T>? = null,
    success: (T) -> Unit,
): T {
    if (dbResult != null) {
        when (dbResult) {
            is DbResult.Success -> {
                success(dbResult.data)
                return dbResult.data
            }
            is DbResult.Error -> {
                throw dbResult.exception
            }
        }
    }

    if (apiResult != null) {
        when (apiResult) {
            is ApiResult.Success -> {
                success(apiResult.data)
                return apiResult.data
            }
            is ApiResult.Error -> {
                throw apiResult.exception
            }
        }
    }
    throw NullPointerException("Result Type Null")
}