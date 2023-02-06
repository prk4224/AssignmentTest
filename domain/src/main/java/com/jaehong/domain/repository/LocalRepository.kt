package com.jaehong.domain.repository

import com.jaehong.domain.model.DbResult
import com.jaehong.domain.model.RecentInfo
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun getRecentList(): Flow<DbResult<List<RecentInfo>>>

    suspend fun insertRecentInfo(info: String)

    suspend fun deleteRecentInfoList(recentList: List<RecentInfo>)
}