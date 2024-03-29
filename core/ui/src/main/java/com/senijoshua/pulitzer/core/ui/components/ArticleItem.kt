package com.senijoshua.pulitzer.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.senijoshua.pulitzer.core.model.PresentationArticle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import java.util.Date

/**
 * Article card composable that can be reused in across feature modules.
 */
@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: PresentationArticle,
    onArticleItemClicked: (String) -> Unit = {},
) {
    OutlinedCard(
        modifier = modifier.cardModifier(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            dimensionResource(id = R.dimen.density_1),
            MaterialTheme.colorScheme.onSurface
        ),
        onClick = {
            onArticleItemClicked(article.id)
        }
    ) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(article.thumbnail)
            .memoryCacheKey(article.thumbnail)
            .placeholder(R.drawable.ic_article_placeholder)
            .error(R.drawable.ic_article_placeholder)
            .fallback(R.drawable.ic_article_placeholder)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(article.thumbnail)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()

        var isBookmarked by remember { mutableStateOf(article.isBookmarked) }

        Row(
            modifier = modifier
                .cardRowModifier(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.density_4)
            )
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.density_64))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.density_2))),
                model = imageRequest,
                contentDescription = stringResource(id = R.string.article_thumbnail),
                contentScale = ContentScale.Crop,
            )

            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = article.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = {
                    isBookmarked = !isBookmarked
                }) {
                val painter: Painter
                val iconColor: Color

                if (isBookmarked) {
                    painter = painterResource(id = R.drawable.ic_bookmark_filled)
                    iconColor = MaterialTheme.colorScheme.primary
                } else {
                    painter = painterResource(id = R.drawable.ic_bookmark_outlined)
                    iconColor = MaterialTheme.colorScheme.outline
                }

                Icon(
                    painter = painter,
                    tint = iconColor,
                    contentDescription = stringResource(id = R.string.article_bookmark)
                )
            }
        }
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun ArticleItemPreview() {
    PulitzerTheme {
        ArticleItem(
            article = PresentationArticle(
                id = "article_id",
                thumbnail = "",
                title = "Top 50 places to travel in the world that have stores",
                author = "John Storm",
                isBookmarked = false,
                lastModified = Date(System.currentTimeMillis())
            )
        )
    }
}

@Composable
private fun Modifier.cardModifier() = this
    .padding(
        vertical = dimensionResource(id = R.dimen.density_8),
        horizontal = dimensionResource(id = R.dimen.density_16)
    )
    .fillMaxWidth()
    .height(dimensionResource(id = R.dimen.density_72))


@Composable
private fun Modifier.cardRowModifier() = this
    .fillMaxSize()
    .padding(
        horizontal = dimensionResource(id = R.dimen.density_8),
        vertical = dimensionResource(id = R.dimen.density_8)
    )
