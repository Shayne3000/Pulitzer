@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.details

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.text.util.LinkifyCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.material.textview.MaterialTextView
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.components.TextCircle
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.core.ui.util.buildAsyncImage
import com.senijoshua.pulitzer.feature.details.model.DetailArticle
import com.senijoshua.pulitzer.feature.details.model.fakeDetailArticle
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "MMMM d"
private const val TITLE = "Title"
private const val AUTHOR = "Author"
private const val DATE = "Date"
private const val BODY = "Body"

@Composable
internal fun DetailScreen(
    vm: DetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    DetailContent(uiState = uiState, bookmarkArticle = { articleId ->
        vm.bookmarkArticle(articleId)
    }, onErrorMessageShown = {
        vm.errorMessageShown()
    }, onBackClicked = {
        onBackClicked()
    })

    LaunchedEffect(Unit) {
        vm.getArticle()
    }
}

@Composable
internal fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUiState,
    bookmarkArticle: (String) -> Unit = {},
    onErrorMessageShown: () -> Unit = {},
    onBackClicked: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = { onBackClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_content_desc),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(dimensionResource(id = R.dimen.density_36)),
                        painter = painterResource(id = R.drawable.guardian_logo),
                        contentDescription = stringResource(R.string.guardian_logo_content_desc)
                    )
                    Text(
                        text = stringResource(id = R.string.published_guardian),
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
            )
        )
    }, snackbarHost = {
        SnackbarHost(snackBarHostState)
    }, bottomBar = {
        BottomAppBar(
            modifier = Modifier.height(dimensionResource(id = R.dimen.density_64)),
            actions = {
                val isBookmarked = uiState.details?.isBookmarked ?: false

                IconButton(onClick = {
                    bookmarkArticle(uiState.details?.id!!)
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
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.density_4)),
                        painter = painter,
                        tint = iconColor,
                        contentDescription = stringResource(id = R.string.article_bookmark)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                PulitzerProgressIndicator(modifier)
            } else if (uiState.details != null) {
                ArticleDetail(article = uiState.details)
            } else {
                EmptyScreen(
                    modifier,
                    text = R.string.no_article_text,
                    iconContentDescription = R.string.empty_article_details
                )
            }
        }
        uiState.errorMessage?.let { errorMessage ->
            LaunchedEffect(snackBarHostState, errorMessage) {
                snackBarHostState.showSnackbar(errorMessage)
                onErrorMessageShown()
            }
        }
    }
}

@Composable
internal fun ArticleDetail(
    modifier: Modifier = Modifier,
    article: DetailArticle,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = buildAsyncImage(imageUrl = article.thumbnail),
            contentDescription = stringResource(id = R.string.detail_article_hero_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.density_230))
                .padding(
                    horizontal = dimensionResource(id = R.dimen.density_16),
                    vertical = dimensionResource(
                        id = R.dimen.density_8
                    )
                )
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.density_4)))
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.density_16))
                .testTag(TITLE),
            text = article.title,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.density_16),
                    vertical = dimensionResource(
                        id = R.dimen.density_8
                    )
                ), verticalAlignment = Alignment.CenterVertically
        ) {
            val author = article.author
            if (!author.isNullOrEmpty()) {
                TextCircle(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.density_8)),
                    text = author.split(" ", limit = 1)[0]
                )
            }

            Column {
                article.author?.let { author ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(AUTHOR),
                        text = author,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                val lastModifiedDate =
                    SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(article.lastModified)

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(DATE),
                    text = lastModifiedDate,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        val articleBody =
            HtmlCompat.fromHtml(article.body, 0)

        val linkColor: Int = MaterialTheme.colorScheme.primary.toArgb()

        val bodyTextColor: Int = MaterialTheme.colorScheme.onSurface.toArgb()

        val context = LocalContext.current

        val articleText = remember {
            MaterialTextView(context)
        }

        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.density_16)
            )
            .testTag(BODY),
            factory = {
                LinkifyCompat.addLinks(articleText, Linkify.WEB_URLS)

                articleText.apply {
                    setTextColor(bodyTextColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    articleText.setLinkTextColor(linkColor)
                    articleText.movementMethod = LinkMovementMethod.getInstance()
                }
            },
            update = { materialTextView ->
                materialTextView.text = articleBody
            })
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun DetailScreenPreview() {
    PulitzerTheme {
        DetailContent(uiState = DetailUiState(details = fakeDetailArticle, isLoading = false))
    }
}
