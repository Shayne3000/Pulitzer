package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.core.ui.util.buildAsyncImage
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import com.senijoshua.pulitzer.feature.bookmarks.model.fakeBookmarkedArticles

@Composable
internal fun BookmarksArticleItem(
    modifier: Modifier = Modifier,
    article: BookmarksArticle,
    searchQuery: String = "",
    isSelected: Boolean = false,
) {
    OutlinedCard(
        modifier = modifier
            .width(dimensionResource(R.dimen.density_192)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            dimensionResource(id = if (isSelected) R.dimen.density_4 else R.dimen.density_1),
            if (isSelected) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        ),
    ) {
        Column {
            val rotation: Float by animateFloatAsState(
                targetValue = if (isSelected) 180f else 0f,
                animationSpec = tween(durationMillis = 345),
                label = "Rotation"
            )

            val density = LocalDensity.current

            BookmarkArticleHeader(
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                    cameraDistance = with(density) { 12.dp.toPx() }
                },
                article = article,
                rotationValue = rotation,
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            HighlightingText(
                text = article.title,
                textToHighlight = searchQuery,
            )

            if (!article.author.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    text = article.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun BookmarkArticleHeader(
    modifier: Modifier = Modifier,
    article: BookmarksArticle,
    rotationValue: Float,
) {
    val thumbnailShape = RoundedCornerShape(
        topEnd = dimensionResource(id = R.dimen.density_12),
        topStart = dimensionResource(id = R.dimen.density_12),
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    )

    Box(
        modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.density_148))
            .clip(thumbnailShape)
    ) {
        if (rotationValue > 90f) {
            BookmarkedArticleSelectedCheck()
        } else {
            BookmarkedArticleImage(article = article)
        }
    }
}

@Composable
private fun BookmarkedArticleImage(
    modifier: Modifier = Modifier,
    article: BookmarksArticle,
) {
    AsyncImage(
        modifier = modifier
            .fillMaxSize(),
        model = buildAsyncImage(imageUrl = article.thumbnail),
        contentDescription = stringResource(id = R.string.article_thumbnail_content_desc),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun BookmarkedArticleSelectedCheck(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .graphicsLayer { rotationY = 180f },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.density_64)),
            imageVector = Icons.Outlined.CheckCircle,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(R.string.selected_bookmarked_article_content_desc)
        )
    }
}

/**
 *  Text Composable that highlights the words in [text]
 *  that matches [textToHighlight] provided that the
 *  words are sequential.
 */
@Composable
private fun HighlightingText(
    text: String,
    textToHighlight: String,
) {
    val highlightColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    val annotatedTitle = remember(text, textToHighlight) {
        if (textToHighlight.isBlank()) {
            buildAnnotatedString {
                withStyle(SpanStyle(color = textColor)) {
                    append(text)
                }
            }
        } else {
            val lowerTitle = text.lowercase()
            val lowerQuery = textToHighlight.lowercase()
            // Split textToHighlight into words trimming all whitespace in tandem
            val words = lowerQuery.split("\\s+".toRegex())

            buildAnnotatedString {
                var currentPosition = 0

                // We're basically matching the next match index to the currentPosition
                // to determine from which index to start highlighting and
                // how many characters will be highlighted.
                while (currentPosition < text.length) {
                    // We'll only highlight if this remains true
                    // after cycling through the words in textToHighlight
                    var allWordsMatch = true
                    // Number of characters in the title that match the query per time
                    var matchCharacterCount = 0

                    for (word in words) {
                        val nextMatchIndex = lowerTitle.indexOf(
                            word,
                            currentPosition + matchCharacterCount
                        )

                        if (nextMatchIndex != currentPosition + matchCharacterCount) {
                            // The next match index does not match the word at currentPosition
                            // so break out of for-loop because we shall not highlight starting from
                            // this index.
                            allWordsMatch = false
                            break
                        }

                        matchCharacterCount += word.length

                        // Account for the space between words, unless it's the last word
                        if (word != words.last()) {
                            // Gate to prevent index out-of-bounds error in lowerTitle[cp + mcm],
                            // for the edge case where the length of textToHighlight exceeds text
                            // and one or more words in textToHighlight matches.
                            if (currentPosition + matchCharacterCount >= text.length ||
                                lowerTitle[currentPosition + matchCharacterCount] != ' ') {
                                // There's no space after this word so break out of for-loop
                                allWordsMatch = false
                                break
                            }
                            // There's space after this word so increase match character count
                            matchCharacterCount++
                        }
                    }

                    if (allWordsMatch) {
                        // Highlight the matching characters with the highlighted color
                        // and append it to the builder for the annotated title.
                        withStyle(SpanStyle(color = highlightColor)) {
                            append(text.substring(currentPosition, currentPosition + matchCharacterCount))
                        }
                        currentPosition += matchCharacterCount
                    } else {
                        // Highlight character at the currentPosition index in the text
                        // with the un-highlighted color and append it to the builder for
                        // the annotated title.
                        withStyle(style = SpanStyle(color = textColor)) {
                            append(text[currentPosition].toString())
                        }
                        currentPosition++
                    }
                }
            }
        }
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.density_8),
                end = dimensionResource(id = R.dimen.density_8),
                top = dimensionResource(id = R.dimen.density_8)
            ),
        text = annotatedTitle,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun BookmarksArticleItemPreview() {
    PulitzerTheme {
        BookmarksArticleItem(
            modifier = Modifier.padding(24.dp),
            article = fakeBookmarkedArticles[0],
        )
    }
}
