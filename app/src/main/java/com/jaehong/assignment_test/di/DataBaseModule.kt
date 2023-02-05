package com.jaehong.assignment_test.di

import android.content.Context
import androidx.room.Room
import com.jaehong.data.local.database.SearchAppDataBase
import com.jaehong.data.local.database.SearchAppDataBase.Companion.SEARCH_APP_NAME
import com.jaehong.data.local.database.dao.RecentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideKoreanHistoryDataBase(
        @ApplicationContext context: Context
    ): SearchAppDataBase {
        return Room.databaseBuilder(
            context,
            SearchAppDataBase::class.java,
            SEARCH_APP_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideMyStudy(
        dataBase: SearchAppDataBase
    ): RecentDao = dataBase.recentDao()

}