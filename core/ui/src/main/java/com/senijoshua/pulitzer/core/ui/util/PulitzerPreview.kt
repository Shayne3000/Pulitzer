package com.senijoshua.pulitzer.core.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Custom multipreview annotation for Light and Dark theme modes of UI components in the app
 * wrapped in a system UI.
 */
@Preview(name = "A: Light Mode", showSystemUi = true)
@Preview(name = "B: Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewPulitzerLightDarkSystemUi

/**
 * Custom multipreview annotation for Light and Dark theme modes of UI components in the app
 * wrapped in a background.
 */
@Preview(name = "A: Light Mode", showBackground = true)
@Preview(name = "B: Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewPulitzerLightDarkBackground
