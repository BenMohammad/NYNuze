package com.benmohammad.nynuze.data.repository

import com.benmohammad.nynuze.API_KEY
import com.benmohammad.nynuze.data.dataModels.NewsResponse
import com.benmohammad.nynuze.network.NYApi
import io.reactivex.Single
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val nyApi: NYApi) {

    fun fetchHomeNews(): Single<NewsResponse> {
        return nyApi.fetchHomeNews(API_KEY)
    }

    fun fetchMovieNews(): Single<NewsResponse> {
        return nyApi.fetchMovieNews(API_KEY)
    }

    fun fetchScienceNews(): Single<NewsResponse> {
        return nyApi.fetchScienceNews(API_KEY)
    }

    fun fetchSportsNews(): Single<NewsResponse> {
        return nyApi.fetchScienceNews(API_KEY)
    }
}