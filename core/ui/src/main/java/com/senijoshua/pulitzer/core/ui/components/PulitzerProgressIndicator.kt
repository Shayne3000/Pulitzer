package com.senijoshua.pulitzer.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkSystemUi

const val PROGRESS_TAG: String = "PulitzerProgress"

@Composable
fun PulitzerProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .testTag(PROGRESS_TAG)
            .size(dimensionResource(id = R.dimen.density_64)),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
@PreviewPulitzerLightDarkSystemUi
private fun PulitzerProgressIndicatorPreview() {
    PulitzerTheme {
        PulitzerProgressIndicator()
    }
}
