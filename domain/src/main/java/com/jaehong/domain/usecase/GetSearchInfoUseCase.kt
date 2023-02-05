package com.jaehong.domain.usecase

import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.DbResult
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.repository.LocalRepository
import com.jaehong.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.awt.SystemColor.info
import javax.inject.Inject

class GetSearchInfoUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(
        keyword: String
    ): Flow<ApiResult<MovieItems>> = flow {
        remoteRepository.getSearchMovie(keyword).collect {
            emit(it)
        }
    }

    suspend fun insertRecentInfo(
        info: String
    ): Flow<DbResult<String>> = flow {
        localRepository.insertRecentInfo(info).collect {
            emit(it)
        }
    }
}