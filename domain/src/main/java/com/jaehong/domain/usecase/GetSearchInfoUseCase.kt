package com.jaehong.domain.usecase

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.repository.LocalRepository
import com.jaehong.domain.repository.RemoteRepository
import javax.inject.Inject

class GetSearchInfoUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(
        keyword: String,
        start: Int
    ): ApiResult<MovieItems> {
        return remoteRepository.getSearchMovie(keyword,start)
    }

    suspend fun insertRecentInfo(info: String) {
        localRepository.insertRecentInfo(info)
    }
}