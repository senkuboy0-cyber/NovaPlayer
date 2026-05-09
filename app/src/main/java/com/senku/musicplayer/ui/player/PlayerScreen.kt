package com.senku.musicplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senku.musicplayer.player.PlayerManager

@Composable
fun PlayerScreen() {
    val currentSong = PlayerManager.currentSong.value
    val isPlaying = PlayerManager.isPlaying.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E1E2A),
                        Color(0xFF0F0F1A)
                    )
                )
            )
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Now Playing",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Album Art Placeholder
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF2C2C3E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_play),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF6C63FF)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = currentSong?.title ?: "No Song Playing",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentSong?.artist ?: "Unknown Artist",
            color = Color.Gray,
            fontSize = 16.sp,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(32.dp))

        Slider(
            value = 0f,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6C63FF),
                activeTrackColor = Color(0xFF6C63FF),
                inactiveTrackColor = Color.DarkGray
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0:00", color = Color.Gray, fontSize = 12.sp)
            Text("0:00", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = "Previous",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { PlayerManager.previous() },
                tint = Color.White
            )

            Surface(
                shape = CircleShape,
                color = Color(0xFF6C63FF),
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        if (isPlaying) PlayerManager.pause() else PlayerManager.resume()
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            id = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                        ),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            }

            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_next),
                contentDescription = "Next",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { PlayerManager.next() },
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
