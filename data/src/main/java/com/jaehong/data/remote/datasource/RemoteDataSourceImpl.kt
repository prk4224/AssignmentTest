package com.jaehong.data.remote.datasource

import com.jaehong.data.remote.network.NaverApiService
import com.jaehong.data.util.safeApiCall
import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: NaverApiService
) : RemoteDataSource {

    override suspend fun getSearchMovie(
        keyword: String,
        start: Int,
    ): ApiResult<MovieItems> {
        return safeApiCall { service.getSearchMovie(keyword,(start - 1) * 10 + 1) }
    }
}