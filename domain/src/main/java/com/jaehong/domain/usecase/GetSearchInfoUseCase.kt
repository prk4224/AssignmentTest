package com.jaehong.domain.usecase

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchInfoUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(
        keyword: String
    ): Flow<ApiResult<MovieItems>> = flow {
        searchRepository.getSearchMovie(keyword).collect{
            emit(it)
        }
    }
}