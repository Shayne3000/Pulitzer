package com.senijoshua.pulitzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PulitzerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulitzerTheme {
                Pulitzer()
            }
        }
    }
}
