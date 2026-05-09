package com.senku.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.senku.musicplayer.ui.player.VideoPlayerScreen
import com.senku.musicplayer.ui.theme.NovaPlayerTheme

class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uriString = intent.getStringExtra("video_uri")
        setContent {
            NovaPlayerTheme {
                VideoPlayerScreen(uriString = uriString)
            }
        }
    }
}
