package com.jaehong.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaehong.data.local.database.dao.RecentDao
import com.jaehong.data.local.database.entity.RecentEntity

@Database(
    entities = [
        RecentEntity::class
    ],
    version = 1
)
abstract class SearchAppDataBase: RoomDatabase() {

    abstract fun recentDao(): RecentDao

    companion object {
        const val SEARCH_APP_NAME = "SearchApp.db"
    }
}