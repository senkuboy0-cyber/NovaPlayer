package com.senku.musicplayer.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senku.musicplayer.VideoPlayerActivity
import com.senku.musicplayer.player.PlayerManager
import com.senku.musicplayer.ui.components.MiniPlayer
import com.senku.musicplayer.ui.components.SongItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("Audio") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212),
                        Color(0xFF050505)
                    )
                )
            )
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "NovaPlayer",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Your Offline Media",
                    color = Color.Gray
                )
            }

            Text(
                text = "🎵",
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedTab = "Audio" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "Audio") Color(0xFF6C63FF) else Color.DarkGray
                )
            ) {
                Text("Audio")
            }
            Button(
                onClick = { selectedTab = "Video" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "Video") Color(0xFF6C63FF) else Color.DarkGray
                )
            ) {
                Text("Video")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            val filteredSongs = viewModel.songs.filter { it.isVideo == (selectedTab == "Video") }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x22FFFFFF)
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "${selectedTab}s Found: ${filteredSongs.size}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredSongs) { song ->
                    SongItem(
                        song = song,
                        onClick = {
                            if (song.isVideo) {
                                val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                                    putExtra("video_uri", song.uri.toString())
                                }
                                context.startActivity(intent)
                            } else {
                                PlayerManager.playSong(context, song)
                            }
                        }
                    )
                }
            }

            if (selectedTab == "Audio") {
                MiniPlayer()
            }
        }
    }
}
