package com.senku.musicplayer.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlayerScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Now Playing")

        Button(onClick = {}) {
            Text(text = "Play")
        }
    }
}
