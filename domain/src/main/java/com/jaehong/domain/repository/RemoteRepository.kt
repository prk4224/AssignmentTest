package com.jaehong.domain.repository

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun getSearchMovie(
        keyword: String,
    ): Flow<ApiResult<MovieItems>>
}