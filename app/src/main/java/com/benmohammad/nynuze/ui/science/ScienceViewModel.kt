package com.benmohammad.nynuze.ui.science

import androidx.lifecycle.ViewModel
import com.benmohammad.nynuze.network.Lce
import com.benmohammad.nynuze.viewState.NewsViewEvent
import com.benmohammad.nynuze.viewState.NewsViewResult
import com.benmohammad.nynuze.viewState.NewsViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ScienceViewModel @Inject constructor(private val scienceRepository: ScienceRepository): ViewModel() {

    private val eventEmitter: PublishSubject<NewsViewEvent> = PublishSubject.create()
    private lateinit var disposable: Disposable
    val viewState: Observable<NewsViewState>

    init {
        eventEmitter
                .eventToResult()
                .share()
                .also { result ->
                    viewState = result
                            .resultToViewState()
                            .replay(1)
                            .autoConnect(1) {disposable = it}


                }
    }


    fun processInput(movieViewEvent: NewsViewEvent?) {
        eventEmitter.onNext(movieViewEvent ?: NewsViewEvent.ScreenLoadEvent)
    }

    private fun Observable<NewsViewEvent>.eventToResult(): Observable<Lce<out NewsViewResult>> {
        return publish {o -> o.ofType(NewsViewEvent.ScreenLoadEvent::class.java).onScreenLoad()
        }
    }

    private fun Observable<Lce< out NewsViewResult>>.resultToViewState(): Observable<NewsViewState> {
        return scan(NewsViewState()) { vs, result ->
            when(result) {
                is Lce.Content -> {
                    when(result.packet) {
                        is NewsViewResult.ScreenLoadResult -> {
                            vs.copy(isLoading = false, isEmpty = false, adapterList = result.packet.list, error = "")
                        }
                        else -> {
                            error("Invalid event result")
                        }
                    }
                }
                is Lce.Loading -> {
                    vs.copy(isLoading = true, error = "")
                }

                is Lce.Error -> {
                    when(result.packet) {
                        is NewsViewResult.ScreenLoadResult -> {
                            if(result.packet.list.isEmpty()) {
                                vs.copy(isLoading = false, isEmpty = true, error = result.packet.error)
                            }else {
                                vs.copy(isLoading = false, error = result.packet.error)
                            }
                        }
                        else -> {
                            error("Invalid event result")
                        }
                    }
                }
            }
        }
    }

    private fun Observable<NewsViewEvent.ScreenLoadEvent>.onScreenLoad(): Observable<Lce<out NewsViewResult>> {
        return switchMap {
            scienceRepository.getScienceNews()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}