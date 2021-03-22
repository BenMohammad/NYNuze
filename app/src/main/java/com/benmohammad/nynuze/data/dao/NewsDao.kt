package com.benmohammad.nynuze.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.benmohammad.nynuze.*
import com.benmohammad.nynuze.data.entity.News
import io.reactivex.Observable

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    fun saveNews(news : Array<News>)

    @Query(GET_ALL_HOME_NEWS_QUERY)
    fun getAllHomeNews(newsType: String): Observable<List<News>>

    @Query(GET_ALL_MOVIE_NEWS_QUERY)
    fun getAllMovieNews(newsType: String): Observable<List<News>>

    @Query(GET_ALL_SCIENCE_NEWS_QUERY)
    fun getAllScienceNews(newsType: String): Observable<List<News>>

    @Query(GET_ALL_SPORTS_NEWS_QUERY)
    fun getAllSportsNews(newsType: String): Observable<List<News>>

    @Query(GET_HOME_NEWS_DETAILS_QUERY)
    fun getNewsDetails(id: String): Observable<News>

}