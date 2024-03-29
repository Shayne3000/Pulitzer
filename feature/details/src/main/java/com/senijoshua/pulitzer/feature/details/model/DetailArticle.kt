package com.senijoshua.pulitzer.feature.details.model

import java.util.Date

/**
 * Presentation layer representation of an Article type with data relevant to the article detail
 * screen
 */
internal data class DetailArticle(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val body: String,
    val isBookmarked: Boolean,
    val lastModified: Date,
)
