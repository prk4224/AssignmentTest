package com.jaehong.data.remote.network

import com.jaehong.domain.model.MovieItems
import com.jaehong.data.remote.util.Constants.MOVIE_JSON
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApiService {

    @GET(MOVIE_JSON)
    suspend fun getSearchMovie(
        @Query("query") keyword: String,
    ): Response<MovieItems>
}