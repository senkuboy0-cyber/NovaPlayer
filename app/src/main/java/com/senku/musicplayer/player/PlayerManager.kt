package com.senku.musicplayer.player

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.senku.musicplayer.data.model.Song

object PlayerManager {

    private var exoPlayer: ExoPlayer? = null

    val currentSong = mutableStateOf<Song?>(null)
    val isPlaying = mutableStateOf(false)

    fun initialize(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun playSong(context: Context, song: Song) {

        initialize(context)

        currentSong.value = song

        val mediaItem = MediaItem.fromUri(song.uri)

        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.play()

        isPlaying.value = true
    }

    fun pause() {
        exoPlayer?.pause()
        isPlaying.value = false
    }

    fun resume() {
        exoPlayer?.play()
        isPlaying.value = true
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
