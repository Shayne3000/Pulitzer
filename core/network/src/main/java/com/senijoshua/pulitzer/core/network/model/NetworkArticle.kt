package com.senijoshua.pulitzer.core.network.model

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ArticleResponse(val response: NetworkArticleResults)

@JsonClass(generateAdapter = true)
data class NetworkArticleResults(val results: List<NetworkArticle>)

@JsonClass(generateAdapter = true)
data class NetworkArticle(val id: String, val fields: NetworkArticleFields)

@JsonClass(generateAdapter = true)
data class NetworkArticleFields(
    val headline: String, // Title
    val byline: String?, // author
    val body: String, // Body
    val thumbnail: String?, // TODO Big photo ends with 1000.jpg, thumbnails are 500.jpg
    val lastModified: Date,
)
