package com.senijoshua.pulitzer.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme

const val PROGRESS_TAG: String = "PulitzerProgress"

@Composable
fun PulitzerProgressIndicator(modifier: Modifier = Modifier, size: Dp) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag(PROGRESS_TAG)
                .size(size),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PulitzerProgressIndicatorPreview() {
    PulitzerTheme {
        PulitzerProgressIndicator(size = dimensionResource(id = R.dimen.density_64))
    }
}
