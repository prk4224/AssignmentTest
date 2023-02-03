package com.jaehong.data.remote.repository

import com.jaehong.data.remote.datasource.SearchDataSource
import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: SearchDataSource
): SearchRepository {
    override suspend fun getSearchMovie(
        keyword: String
    ): Flow<ApiResult<MovieItems>> = flow {
        dataSource.getSearchMovie(keyword).collect {
            emit(it)
        }
    }
}