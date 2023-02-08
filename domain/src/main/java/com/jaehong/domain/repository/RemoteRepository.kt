package com.jaehong.domain.repository

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems

interface RemoteRepository {
    suspend fun getSearchMovie(
        keyword: String,
        start: Int,
    ): ApiResult<MovieItems>
}