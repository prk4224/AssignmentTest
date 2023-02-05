package com.jaehong.domain.usecase

import com.jaehong.domain.model.DbResult
import com.jaehong.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke()
    : Flow<DbResult<List<String>>> = flow {
        localRepository.getRecentList().collect {
            emit(it)
        }
    }

    suspend fun deleteRecentInfo()
            : Flow<DbResult<String>> = flow {
        localRepository.deleteLastInfo().collect {
            emit(it)
        }
    }
}