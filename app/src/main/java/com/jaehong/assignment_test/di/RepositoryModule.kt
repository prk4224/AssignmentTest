package com.jaehong.assignment_test.di

import com.jaehong.data.remote.datasource.SearchDataSource
import com.jaehong.data.remote.datasource.SearchDataSourceImpl
import com.jaehong.data.remote.repository.SearchRepositoryImpl
import com.jaehong.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindSearchRepository(searchRepository: SearchRepositoryImpl): SearchRepository

    @Singleton
    @Binds
    fun bindSearchDataSource(searchDataSourceImpl: SearchDataSourceImpl): SearchDataSource
}