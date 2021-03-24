package com.benmohammad.nynuze.ui.science

import com.benmohammad.nynuze.COVER_PHOTO
import com.benmohammad.nynuze.FETCH_TIME_OUT
import com.benmohammad.nynuze.SCIENCE_NEWS
import com.benmohammad.nynuze.THUMBNAIL
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
import kotlin.math.abs

class ScienceRepository @Inject constructor(private val localRepository: LocalRepository,
                                            private val remoteRepository: RemoteRepository,
                                            private val sessionManager: SessionManager) {

    fun getScienceNews(): Observable<Lce<NewsViewResult.ScreenLoadResult>>? {
        if(!shouldFetch()) {
            return localRepository.getScienceNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if(it.isNullOrEmpty()) {
                            Lce.Error(NewsViewResult.ScreenLoadResult(it, "empty list..."))
                        } else {
                            Lce.Content(NewsViewResult.ScreenLoadResult(it))
                        }
                    }.startWith(Lce.Loading())
        }else {
            return remoteRepository.fetchScienceNews()
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
                                        newsType = SCIENCE_NEWS)
                                newsArray.add(homeNews)
                            }
                            localRepository.insertNewsItem(newsArray.toTypedArray())
                            sessionManager.lastFetchTimeScienceNews = Calendar.getInstance().timeInMillis
                        }
                    }.flatMapObservable {
                        localRepository.getScienceNews()
                    }.map {
                        if(it.isNullOrEmpty()) {
                            Lce.Error(NewsViewResult.ScreenLoadResult(it, "empty list..."))
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
        return (Calendar.getInstance().timeInMillis - (sessionManager.lastFetchTimeScienceNews ?: 0L) > FETCH_TIME_OUT)
    }
}