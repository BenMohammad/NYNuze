package com.benmohammad.nynuze.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benmohammad.nynuze.network.Lce
import com.benmohammad.nynuze.viewState.DetailViewEffect
import com.benmohammad.nynuze.viewState.DetailViewEvent
import com.benmohammad.nynuze.viewState.DetailViewResult
import com.benmohammad.nynuze.viewState.DetailViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository): ViewModel() {
    private val eventEmitter: PublishSubject<DetailViewEvent> = PublishSubject.create()
    val viewState: Observable<DetailViewState>
    val viewEffects: Observable<DetailViewEffect>
    private lateinit var disposable: Disposable

    init {
        eventEmitter
                .eventToResult()
                .share()
                .also { result ->
                    viewState = result
                            .resultToViewState()
                            .replay(1)
                            .autoConnect(1) {disposable = it}
                    viewEffects = result
                            .resultToEffect()
                }
    }

    fun processInput(it: DetailViewEvent) {
        eventEmitter.onNext(it)
    }

    private fun Observable<Lce<out DetailViewResult>>.resultToEffect(): Observable<DetailViewEffect> {
        return filter { it is Lce.Content && it.packet is DetailViewResult.OpenChromeResult }
                .map<DetailViewEffect> {
                    when(it) {
                        is Lce.Content -> {
                            when(it.packet) {
                                is DetailViewResult.OpenChromeResult -> {
                                    DetailViewEffect.OpenChromeEffect(it.packet.url)
                                }
                                else -> {
                                    DetailViewEffect.OpenChromeEffect("")
                                }
                            }
                        }
                        else -> {
                        DetailViewEffect.OpenChromeEffect("")
                        }
                    }
                }
    }

    private fun Observable<Lce <out DetailViewResult>>.resultToViewState(): Observable<DetailViewState> {
        return scan(DetailViewState()) {
            vs, result ->
            when(result) {
                is Lce.Content -> {
                    when(result.packet) {
                        is DetailViewResult.LoadDetailResult -> {
                            val details = result.packet.generalNews
                            vs.copy(
                                    isLoading = false,
                                    title = details?.title,
                                    abstract = details.abstract,
                                    coverPhoto = details.coverImage,
                                    author = details.author,
                                    link = details.articleLink,
                                    published = details.publishedOn
                            )
                        }
                        is DetailViewResult.OpenChromeResult -> {
                            vs.copy(isLoading = false)
                        }
                    }
                }
                is Lce.Error -> {
                    when(result.packet) {
                        is DetailViewResult.LoadDetailResult -> {
                            vs.copy(isLoading = false, error = result.packet.error)
                        }
                        is DetailViewResult.OpenChromeResult -> {
                            vs.copy(isLoading = false)
                        }
                        }
                    }
                is Lce.Loading -> {
                    vs.copy(isLoading = true)

                }
            }
        }
    }

    private fun Observable<DetailViewEvent>.eventToResult(): Observable<Lce<out DetailViewResult>> {
        return publish {
            Observable.merge(
                    it.ofType(DetailViewEvent.LoadDetailEvent::class.java).onLoadDetails(),
                    it.ofType(DetailViewEvent.OpenChromeEvent::class.java).onOpenLink()
            )
        }
    }

    private fun Observable<DetailViewEvent.OpenChromeEvent>.onOpenLink(): Observable<Lce<DetailViewResult>> {
        return map { Lce.Content(DetailViewResult.OpenChromeResult(it.uri)) }
    }

    private fun Observable<DetailViewEvent.LoadDetailEvent>.onLoadDetails(): Observable<Lce<out DetailViewResult>> {
        return switchMap {
            detailRepository.getDetails(it.newsId, it.newsType)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}