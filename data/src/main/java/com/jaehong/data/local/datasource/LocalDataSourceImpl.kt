package com.jaehong.data.local.datasource

import com.jaehong.data.local.database.dao.RecentDao
import com.jaehong.data.local.database.entity.RecentEntity
import com.jaehong.data.util.Constants.DB_ERROR_MESSAGE
import com.jaehong.data.util.Constants.DB_SUCCESS_MESSAGE
import com.jaehong.domain.model.DbResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val recentDao: RecentDao
): LocalDataSource {

    override suspend fun getRecentList()
    : Flow<DbResult<List<RecentEntity>>> = flow {
        val data = recentDao.getRecentList()
        if(data != null) {
            emit(DbResult.Success(data))
        } else {
            emit(DbResult.Error(Exception(DB_ERROR_MESSAGE)))
        }
    }

    override suspend fun insertRecentInfo(info: String)
    : Flow<DbResult<String>> = flow {
        val result = recentDao.insetRecentInfo(
            RecentEntity(
                info,
                System.currentTimeMillis().toString()
        ))
        if(result != null) {
            emit(DbResult.Success(DB_SUCCESS_MESSAGE))
        } else {
            emit(DbResult.Error(Exception(DB_ERROR_MESSAGE)))
        }
    }

    override suspend fun deleteLastInfo()
    : Flow<DbResult<String>> = flow {
        val result = recentDao.deleteLastInt()
        if(result != null) {
            emit(DbResult.Success(DB_SUCCESS_MESSAGE))
        } else {
            emit(DbResult.Error(Exception(DB_ERROR_MESSAGE)))
        }
    }
}