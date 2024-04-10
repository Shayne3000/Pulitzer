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
    val author: String?,
    val body: String,
    val isBookmarked: Boolean,
    val lastModified: Date,
)

internal val fakeDetailArticle = DetailArticle(
    "articleId 1",
    "Article 1 thumbnail",
    "Article 1 Title: The recent news about the sport is interesting. This is epic!",
    "John Smith",
    "<p>I started St Machar Thistle with my mates. We’d been playing seven-a-side in Aberdeen for a year when we thought about starting an 11-a-side team. I&nbsp;was always decent at football – I’d played at school and got back into it last year when I started university.</p> <p>We began the application process to be part of the Scottish Amateur Cup last year,<strong> </strong>and made a good impression to the board. We even got sponsors. But most of us are university<strong> </strong>students and haven’t been able to commit.<strong> </strong>Sometimes we’ve had to postpone our Saturday matches as we didn’t have the minimum seven players required.</p> <p>Since we joined the league, we’ve lost all our games. We’re still making a team and, as captain, I’m figuring it out a bit. The most stick we get from other teams is not for losing, it’s for not&nbsp;always having enough players to fulfil fixtures. Saturday matches mean everything to most folk in the league; it’s proper Aberdonians and locals who&nbsp;work all week for these games.</p> ",
    false,
    Date(System.currentTimeMillis())
)
