package com.jaehong.data.local.repository

import com.jaehong.data.local.datasource.LocalDataSource
import com.jaehong.data.mapper.Mapper.dataToDomain
import com.jaehong.data.mapper.Mapper.domainToData
import com.jaehong.domain.model.DbResult
import com.jaehong.domain.model.RecentInfo
import com.jaehong.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): LocalRepository {
    override suspend fun getRecentList()
    : Flow<DbResult<List<RecentInfo>>> = flow {
        localDataSource.getRecentList().collect {
            when(it){
                is DbResult.Success -> {
                    emit(DbResult.Success(it.data.map { data -> data.dataToDomain() }))
                }
                is DbResult.Error -> {
                    emit(DbResult.Error(it.exception))
                }
            }
        }
    }

    override suspend fun insertRecentInfo(info: String) {
        localDataSource.insertRecentInfo(info)
    }

    override suspend fun deleteRecentInfoList(recentList: List<RecentInfo>) {
        localDataSource.deleteRecentInfoList(recentList.map { it.domainToData() })
    }
}