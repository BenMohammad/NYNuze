package com.benmohammad.nynuze.ui.details

import com.benmohammad.nynuze.data.repository.LocalRepository
import com.benmohammad.nynuze.network.Lce
import com.benmohammad.nynuze.viewState.DetailViewResult
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailRepository @Inject constructor(private val localRepository: LocalRepository) {
    fun getDetails(id: String, type: String): Observable<Lce<DetailViewResult.LoadDetailResult>> {
        return localRepository.getNewsDetails(id)
                .subscribeOn(Schedulers.io())
                .map {
                    if(it.id == id) {
                        Lce.Content(
                                DetailViewResult.LoadDetailResult(
                                        GeneralNews(
                                                title = it.title,
                                                author = it.author,
                                                thumbnail = it.thumbnail,
                                                abstract = it.abstractSt,
                                                coverImage = it.coverImage,
                                                articleLink = it.articleLink,
                                                publishedOn = it.publishedDate
                                        ))
                        )
                    } else {
                        Lce.Error(
                                DetailViewResult.LoadDetailResult(
                                        GeneralNews(), error = "error"
                                )
                        )
                    }
                }.startWith(Lce.Loading())
    }
}