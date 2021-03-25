package com.benmohammad.nynuze.viewState

import com.benmohammad.nynuze.ui.details.GeneralNews
import java.text.SimpleDateFormat
import java.util.*

data class DetailViewState(
        val isLoading: Boolean = false,
        val error: String? = "",
        val title: String? = "",
        val coverPhoto: String? = "",
        val author: String? = "",
        val published: String? = "",
        val link: String? = "",
        val abstract: String? = ""
) {
    fun dateToFormat(dateTotFormat: String): String? {
        var formattedDate = ""
        if(dateTotFormat.isNotEmpty() && dateTotFormat != "null") {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MM yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateTotFormat)
            formattedDate = outputFormat.format(date)
        }
        return formattedDate
    }
}

sealed class DetailViewEvent {
    data class OpenChromeEvent(val uri: String): DetailViewEvent()
    data class LoadDetailEvent(val newsId: String, val newsType: String): DetailViewEvent()
}

sealed class DetailViewEffect {
    data class OpenChromeEffect(var uri: String): DetailViewEffect()
}

sealed class DetailViewResult {
    data class LoadDetailResult(val generalNews: GeneralNews, val error: String = ""): DetailViewResult()
    data class OpenChromeResult(val url: String): DetailViewResult()
}
