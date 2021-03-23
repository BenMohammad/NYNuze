package com.benmohammad.nynuze.ui.home

import com.benmohammad.nynuze.COVER_PHOTO
import com.benmohammad.nynuze.FETCH_TIME_OUT
import com.benmohammad.nynuze.data.SessionManager
import com.benmohammad.nynuze.data.dataModels.ResultsItem
import com.benmohammad.nynuze.data.entity.News
import com.benmohammad.nynuze.data.repository.LocalRepository
import com.benmohammad.nynuze.data.repository.RemoteRepository
import com.benmohammad.nynuze.network.Lce
import com.benmohammad.nynuze.viewState.NewsViewEvent
import com.benmohammad.nynuze.viewState.NewsViewResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
        private val localRepository: LocalRepository,
        private val remoteRepository: RemoteRepository,
        private val sessionManager: SessionManager
) {

    fun getHomeNews(): Observable<Lce<NewsViewResult.ScreenLoadResult>>? {
        if(!shouldFetch()) {
            return localRepository.getHomeNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if(it.isNullOrEmpty()) {
                            Lce.Error(NewsViewResult.ScreenLoadResult(it, "empty list"))
                        } else {
                            Lce.Content(NewsViewResult.ScreenLoadResult(it))
                        }
                    }.startWith(Lce.Loading())
        } else {
            return remoteRepository.fetchHomeNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if(it.results?.isNullOrEmpty() == false) {
                            val newsArray = mutableListOf<News>()
                            it.results.forEach { item ->
                                val news = News(
                                        id = item.createdDate,
                                        title = item.title,
                                        author = item.byline,
                                        abstractSt = item.abstract,
                                        coverImage = getCoverImage(item),
                                        articleLink = item.url,
                                        thumbnail = getThumbnail(item),
                                        publishedDate = item.publishedDate)
                                newsArray.add(news)
                            }
                            localRepository.insertNewsItem(newsArray.toTypedArray())
                            sessionManager.lastFetchTimeHomeNews = Calendar.getInstance().timeInMillis
                        }
                    }.flatMapObservable {
                        localRepository.getHomeNews()
                    }.map { if(it.isNullOrEmpty()) {
                                Lce.Error(NewsViewResult.ScreenLoadResult(it, "empty list"))
                            } else {
                                Lce.Content(NewsViewResult.ScreenLoadResult(it))
                            }
                    }.onErrorReturn {
                        Lce.Error(NewsViewResult.ScreenLoadResult(emptyList(), error = it.localizedMessage))
                    }.startWith(Lce.Loading())
        }
    }

    private fun getThumbnail(it: ResultsItem): String {
        if(it.multimedia.isNullOrEmpty()) return ""
        return it.multimedia.find {
            it.format == COVER_PHOTO
            }?.url ?: ""
    }

    private fun getCoverImage(it: ResultsItem): String {
        if(it.multimedia.isNullOrEmpty()) return ""
        return it.multimedia.find {
            it.format == COVER_PHOTO
        }?.url ?: ""
    }


    private fun shouldFetch(): Boolean {
        return (Calendar.getInstance().timeInMillis - (sessionManager.lastFetchTimeHomeNews ?: 0L) > FETCH_TIME_OUT)
    }
}