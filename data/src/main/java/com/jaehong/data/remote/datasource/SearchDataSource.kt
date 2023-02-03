package com.jaehong.data.remote.datasource

import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface SearchDataSource {
    suspend fun getSearchMovie(
        keyword: String,
    ): Flow<ApiResult<MovieItems>>
}