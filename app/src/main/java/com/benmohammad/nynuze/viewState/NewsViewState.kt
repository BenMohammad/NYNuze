package com.benmohammad.nynuze.viewState

import com.benmohammad.nynuze.data.entity.News
import io.reactivex.internal.operators.maybe.MaybeIsEmpty

data class NewsViewState(
        val isLoading: Boolean = false,
        val error: String = "",
        val isEmpty: Boolean = false,
        val adapterList: List<News> = emptyList()
)

sealed class NewsViewEvent {
    object ScreenLoadEvent: NewsViewEvent()
}

sealed class NewsViewResult {
    data class ScreenLoadResult(val list: List<News>, val error: String = ""): NewsViewResult()
}
