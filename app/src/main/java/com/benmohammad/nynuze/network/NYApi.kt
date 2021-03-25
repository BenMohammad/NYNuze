package com.benmohammad.nynuze.network

import com.benmohammad.nynuze.data.dataModels.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NYApi {
    @GET("home.json")
    fun fetchHomeNews(@Query("api-key") apiKey: String): Single<NewsResponse>


    @GET("movies.json")
    fun fetchMovieNews(@Query("api-key") apiKey: String): Single<NewsResponse>


    @GET("science.json")
    fun fetchScienceNews(@Query("api-key") apiKey: String): Single<NewsResponse>


    @GET("sports.json")
    fun fetchSportsNews(@Query("api-key") apiKey: String): Single<NewsResponse>
}