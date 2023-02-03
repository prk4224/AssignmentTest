package com.jaehong.assignment_test.di

import android.util.Log
import com.jaehong.assignment_test.Constants.BASE_URL
import com.jaehong.assignment_test.Constants.CLIENT_ID_KEY
import com.jaehong.assignment_test.Constants.CLIENT_ID_VALUE
import com.jaehong.assignment_test.Constants.CLIENT_SECRET_KEY
import com.jaehong.assignment_test.Constants.CLIENT_SECRET_VALUE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor { message ->
        Log.d("API", message)
    }.apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideNaverApiInterceptor(): ApiInterceptor = ApiInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: ApiInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun getClient(
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    class ApiInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader(CLIENT_ID_KEY, CLIENT_ID_VALUE)
                .addHeader(CLIENT_SECRET_KEY, CLIENT_SECRET_VALUE)
                .build()
            return chain.proceed(request)
        }
    }
}