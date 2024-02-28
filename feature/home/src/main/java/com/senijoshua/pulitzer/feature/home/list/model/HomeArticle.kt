package com.senijoshua.pulitzer.feature.home.list.model

import java.util.Date

/**
 * Presentation layer representation of an Article type with data relevant to the home
 * screen.
 * We use different model classes per screen to keep the screen data encapsulated
 * from one another. Home screen only accesses the data it needs and nothing more.
 */
internal data class HomeArticle(
    val id: String,
    val thumbnail: String,
    val title: String,
    val isBookmarked: Boolean,
    val publicationDate: Date,
)
