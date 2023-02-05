package com.jaehong.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RECENT_LIST_TABLE")
data class RecentEntity(
    val keyword: String,
    @PrimaryKey val saveTime: String
)