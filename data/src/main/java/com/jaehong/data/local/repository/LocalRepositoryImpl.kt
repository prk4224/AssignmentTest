package com.jaehong.data.local.repository

import com.jaehong.data.local.datasource.LocalDataSource
import com.jaehong.domain.model.DbResult
import com.jaehong.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): LocalRepository {
    override suspend fun getRecentList()
    : Flow<DbResult<List<String>>> = flow {
        localDataSource.getRecentList().collect {
            when(it){
                is DbResult.Success -> {
                    emit(DbResult.Success(it.data.map { data -> data.keyword }))
                }
                is DbResult.Error -> {
                    emit(DbResult.Error(it.exception))
                }
            }
        }
    }

    override suspend fun insertRecentInfo(
        info: String
    ): Flow<DbResult<String>> = flow {
        localDataSource.insertRecentInfo(info).collect {
            emit(it)
        }
    }

    override suspend fun deleteLastInfo()
    : Flow<DbResult<String>> = flow {
        localDataSource.deleteLastInfo().collect {
            emit(it)
        }
    }
}