package com.jaehong.assignment_test.di

import com.jaehong.presentation.navigation.SearchAppNavigator
import com.jaehong.presentation.navigation.SearchAppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Singleton
    @Binds
    fun bindKoreanHistoryNavigator(appNavigatorImpl: SearchAppNavigatorImpl): SearchAppNavigator
}