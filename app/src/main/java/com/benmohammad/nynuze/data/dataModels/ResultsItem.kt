package com.benmohammad.nynuze.data.dataModels

import com.google.gson.annotations.SerializedName

data class ResultsItem(
        @SerializedName("sebsection")
        val subsection: String = "",
        @SerializedName("item_type")
        val itemType: String = "",
        @SerializedName("section")
        val section: String = "",
        @SerializedName("abstract")
        val abstract: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("des_facet")
        val desFacet: String = "",
        @SerializedName("uri")
        val uri: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("short_url")
        val shortUrl: String = "",
        @SerializedName("material_type_facet")
        val materialTypeFacet: String = "",
        @SerializedName("multimedia")
        val multimedia: List<MultimediaItem>?,
        @SerializedName("geo_facet")
        val geoFacet: List<String>?,
        @SerializedName("updated_date")
        val updatedDate: String = "",
        @SerializedName("created_date")
        val createdDate: String = "",
        @SerializedName("byline")
        val byline: String = "",
        @SerializedName("published_date")
        val publishedDate: String = "",
        @SerializedName("kicker")
        val kicker: String = ""
        )
