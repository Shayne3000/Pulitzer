package com.senijoshua.pulitzer.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.senijoshua.pulitzer.core.ui.R

@Composable
fun TextCircle(
    modifier: Modifier = Modifier,
    text: String
) {
    val firstLetter = text.first().toString().uppercase()

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(dimensionResource(id = R.dimen.density_48))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = firstLetter,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
