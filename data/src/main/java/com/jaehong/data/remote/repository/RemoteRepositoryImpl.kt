package com.jaehong.data.remote.repository

import com.jaehong.data.remote.datasource.RemoteDataSource
import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.repository.RemoteRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val dataSource: RemoteDataSource
): RemoteRepository {
    override suspend fun getSearchMovie(
        keyword: String,
        start: Int,
    ): ApiResult<MovieItems> {
        return dataSource.getSearchMovie(keyword,start)
    }
}