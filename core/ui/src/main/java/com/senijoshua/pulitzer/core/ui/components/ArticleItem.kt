package com.senijoshua.pulitzer.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.senijoshua.pulitzer.core.model.PresentationArticle
import com.senijoshua.pulitzer.core.ui.R

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
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.density_100)),
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
        AsyncImage(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.density_64))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.density_2))),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.thumbnail)
                .memoryCacheKey(article.thumbnail)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCacheKey(article.thumbnail)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = stringResource(id = R.string.article_thumbnail),
            contentScale = ContentScale.Crop,
            // TODO Add a placeholder icon for news images using Painter resource for error, fallback and placeholder
        )
    }
}

@Composable
@Preview
fun ArticleItemPreview() {
    // Use Multipreview
}
