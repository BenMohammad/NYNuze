package com.benmohammad.nynuze.data.dataModels

import com.google.gson.annotations.SerializedName

data class MultimediaItem(
        @SerializedName("copyright")
        val copyright: String = "",
        @SerializedName("subtype")
        val subtype: String = "",
        @SerializedName("format")
        val format: String = "",
        @SerializedName("width")
        val width: Int = 0,
        @SerializedName("caption")
        val caption: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("height")
        val height: Int = 0
        )
