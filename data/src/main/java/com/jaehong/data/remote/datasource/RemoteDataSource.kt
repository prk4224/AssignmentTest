package com.jaehong.data.remote.datasource

import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getSearchMovie(
        keyword: String,
        start: Int,
    ): ApiResult<MovieItems>
}