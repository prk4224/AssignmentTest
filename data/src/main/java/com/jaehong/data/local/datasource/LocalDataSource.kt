package com.jaehong.data.local.datasource

import com.jaehong.data.local.database.entity.RecentEntity
import com.jaehong.domain.model.DbResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getRecentList(): Flow<DbResult<List<RecentEntity>>>

    suspend fun insertRecentInfo(info: String)

    suspend fun deleteRecentInfoList(recentList: List<RecentEntity>)
}