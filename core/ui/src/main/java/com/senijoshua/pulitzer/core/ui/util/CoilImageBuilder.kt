package com.senijoshua.pulitzer.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.senijoshua.pulitzer.core.ui.R

@Composable
fun buildAsyncImage(imageUrl: String) = ImageRequest.Builder(LocalContext.current)
    .data(imageUrl)
    .memoryCacheKey(imageUrl)
    .placeholder(R.drawable.ic_article_placeholder)
    .error(R.drawable.ic_article_placeholder)
    .fallback(R.drawable.ic_article_placeholder)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCacheKey(imageUrl)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
