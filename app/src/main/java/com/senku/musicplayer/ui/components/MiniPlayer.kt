package com.senku.musicplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senku.musicplayer.player.PlayerManager

@Composable
fun MiniPlayer() {

    val song = PlayerManager.currentSong.value ?: return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1C)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1C))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = song.title,
                color = Color.White
            )

            Button(
                onClick = {
                    if (PlayerManager.isPlaying.value) {
                        PlayerManager.pause()
                    } else {
                        PlayerManager.resume()
                    }
                }
            ) {
                Text(
                    text = if (PlayerManager.isPlaying.value) {
                        "Pause"
                    } else {
                        "Play"
                    }
                )
            }
        }
    }
}
