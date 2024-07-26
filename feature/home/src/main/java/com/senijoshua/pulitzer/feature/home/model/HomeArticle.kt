package com.senijoshua.pulitzer.feature.home.model

import com.senijoshua.pulitzer.core.model.PresentationArticle

/**
 * [PresentationArticle] subclass with data specific to the home screen.
 * We use different model classes per screen to keep the screen data encapsulated
 * from one another. Home screen only accesses the data it needs and nothing more.
 *
 * // TODO Perhaps consider removing this as each module may have a different article item
 */
internal data class HomeArticle(
    override val id: String,
    override val thumbnail: String,
    override val title: String,
    override val author: String?,
    override val isBookmarked: Boolean,
) : PresentationArticle(id, thumbnail, title, author, isBookmarked, null)
