package com.benmohammad.nynuze.ui.movies

import com.benmohammad.nynuze.COVER_PHOTO
import com.benmohammad.nynuze.FETCH_TIME_OUT
import com.benmohammad.nynuze.MOVIES_NEWS
import com.benmohammad.nynuze.THUMBNAIL
import com.benmohammad.nynuze.data.SessionManager
import com.benmohammad.nynuze.data.dataModels.ResultsItem
import com.benmohammad.nynuze.data.entity.News
import com.benmohammad.nynuze.data.repository.LocalRepository
import com.benmohammad.nynuze.data.repository.RemoteRepository
import com.benmohammad.nynuze.network.Lce
import com.benmohammad.nynuze.viewState.NewsViewResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val localRepository: LocalRepository,
                                           private val remoteRepository: RemoteRepository,
                                           private val sessionManager: SessionManager) {

    fun getMovieNews(): Observable<Lce<NewsViewResult.ScreenLoadResult>>? {
        if(!shouldFetch()) {
            return localRepository.getMovieNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if(it.isNullOrEmpty()) {
                            Lce.Error(NewsViewResult.ScreenLoadResult(it, "error"))
                        } else {
                            Lce.Content(NewsViewResult.ScreenLoadResult(it))
                        }
                    }.startWith(Lce.Loading())
        } else {
            return remoteRepository.fetchMovieNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if(it.results?.isNullOrEmpty() == false) {
                            val newsArray = mutableListOf<News>()
                            it.results.forEach { item ->
                                val homeNews = News(
                                        id = item.createdDate,
                                        title = item.title,
                                        author = item.byline,
                                        abstractSt = item.abstract,
                                        coverImage = getCoverImage(item),
                                        articleLink = item.url,
                                        thumbnail = getThumbnail(item),
                                        publishedDate = item.publishedDate,
                                        newsType = MOVIES_NEWS)
                                newsArray.add(homeNews)
                            }
                            localRepository.insertNewsItem(newsArray.toTypedArray())
                            sessionManager.lastFetchTimeMovieNews = Calendar.getInstance().timeInMillis
                        }
                    }.flatMapObservable {
                        localRepository.getMovieNews()
                    }.map {
                        if(it.isNullOrEmpty()) {
                            Lce.Error(NewsViewResult.ScreenLoadResult(it, "Error"))
                        } else {
                            Lce.Content(NewsViewResult.ScreenLoadResult(it))
                        }
                    }.onErrorReturn {
                        Lce.Error(
                                NewsViewResult.ScreenLoadResult(
                                        emptyList(),
                                        error = it.localizedMessage
                                )
                        )
                    }.startWith(Lce.Loading())
        }
    }

    private fun getThumbnail(it: ResultsItem): String {
        if(it.multimedia.isNullOrEmpty()) return ""
        return it.multimedia.find {
            it.format == THUMBNAIL
        }?.url ?: ""
    }

    private fun getCoverImage(it: ResultsItem): String {
        if(it.multimedia.isNullOrEmpty()) return ""
        return it.multimedia.find {
            it.format == COVER_PHOTO
        }?.url ?: ""
    }

    private fun shouldFetch(): Boolean {
        return (Calendar.getInstance().timeInMillis - (sessionManager.lastFetchTimeMovieNews ?: 0L) > FETCH_TIME_OUT)
    }
}