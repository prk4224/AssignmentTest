package com.jaehong.domain.repository

import com.jaehong.domain.model.DbResult
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun getRecentList(): Flow<DbResult<List<String>>>

    suspend fun insertRecentInfo(info: String): Flow<DbResult<String>>

    suspend fun deleteLastInfo(): Flow<DbResult<String>>
}