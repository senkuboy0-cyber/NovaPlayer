package com.senku.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.senku.musicplayer.ui.player.PlayerScreen
import com.senku.musicplayer.ui.theme.NovaPlayerTheme

class PlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaPlayerTheme {
                PlayerScreen()
            }
        }
    }
}
