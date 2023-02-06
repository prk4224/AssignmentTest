package com.jaehong.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaehong.data.local.database.entity.RecentEntity

@Dao
interface RecentDao {

    @Query("SELECT * FROM RECENT_LIST_TABLE ORDER BY saveTime DESC")
    suspend fun getRecentList(): List<RecentEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetRecentInfo(info: RecentEntity)

    @Delete
    suspend fun deleteRecentInfo(info: RecentEntity)

    suspend fun deleteRecentInfoList(recentList: List<RecentEntity>): Int {
        recentList.forEach {
            deleteRecentInfo(it)
        }
        return 1
    }

}