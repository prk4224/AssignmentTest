package com.jaehong.data.remote.datasource

import com.jaehong.domain.model.MovieItems
import com.jaehong.data.remote.network.NaverApiService
import com.jaehong.data.remote.util.safeApiCall
import com.jaehong.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchDataSourceImpl @Inject constructor(
    private val service: NaverApiService
) : SearchDataSource {

    override suspend fun getSearchMovie(
        keyword: String
    ): Flow<ApiResult<MovieItems>> = flow {
        emit(safeApiCall { service.getSearchMovie(keyword) } )
    }
}