package com.benmohammad.nynuze.data.repository

import com.benmohammad.nynuze.HOME_NEWS
import com.benmohammad.nynuze.MOVIES_NEWS
import com.benmohammad.nynuze.SCIENCE_NEWS
import com.benmohammad.nynuze.SPORTS_NEWS
import com.benmohammad.nynuze.data.dao.NewsDao
import com.benmohammad.nynuze.data.entity.News
import io.reactivex.Observable
import javax.inject.Inject

class LocalRepository @Inject constructor(private val newsDao: NewsDao) {


    fun getHomeNews(): Observable<List<News>> {
        return newsDao.getAllHomeNews(HOME_NEWS)
    }

    fun insertNewsItem(news: Array<News>) {
        newsDao.saveNews(news)
    }

    fun getNewsDetails(id: String): Observable<News> {
        return newsDao.getNewsDetails(id)
    }

    fun getMovieNews(): Observable<List<News>> {
        return newsDao.getAllMovieNews(MOVIES_NEWS)
    }

    fun getScienceNews(): Observable<List<News>> {
        return newsDao.getAllScienceNews(SCIENCE_NEWS)
    }

    fun getSportsNews(): Observable<List<News>> {
        return newsDao.getAllSportsNews(SPORTS_NEWS)
    }
}