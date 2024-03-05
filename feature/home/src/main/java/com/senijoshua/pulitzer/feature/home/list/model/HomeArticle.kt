package com.senijoshua.pulitzer.feature.home.list.model

import com.senijoshua.pulitzer.core.model.PresentationArticle
import java.util.Date

/**
 * [PresentationArticle] subclass with data specific to the home screen.
 *
 * We use different model classes per screen to keep the screen data encapsulated
 * from one another. Home screen only accesses the data it needs and nothing more.
 */
internal data class HomeArticle(
    override val id: String,
    override val thumbnail: String,
    override val title: String,
    override val isBookmarked: Boolean,
    override val publicationDate: Date,
) : PresentationArticle(id, thumbnail, title, isBookmarked, publicationDate)
