@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.details.model.fakeDetailArticle

@Composable
internal fun DetailScreen(
    vm: DetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    DetailContent(uiState = uiState, bookmarkArticle = {
        vm.bookmarkArticle()
    }, onBackClicked = {
        onBackClicked()
    })
}

@Composable
internal fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUiState,
    bookmarkArticle: () -> Unit = {},
    onBackClicked: () -> Unit = {}
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
            actions = {
                val bookmark = uiState.details?.isBookmarked ?: false

                var isBookmarked by remember { mutableStateOf(bookmark) }

                IconButton(onClick = {
                    isBookmarked = !isBookmarked

                    if (isBookmarked) { // TODO the ui is optimistic
                        bookmarkArticle()
                    }
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
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            // TODO Add a Scrollview
        }
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun DetailScreenPreview() {
    PulitzerTheme {
        DetailContent(uiState = DetailUiState(details = fakeDetailArticle, isLoading = false))
    }
}
