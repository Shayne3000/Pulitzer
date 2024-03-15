package com.senijoshua.pulitzer.domain.article.entity

import java.util.Date

/**
 * Core business entity/object of the application. It's the least
 * likely to change when something external changes.
 */
data class Article(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val body: String,
    val isBookmarked: Boolean,
    val publicationDate: Date,
)
