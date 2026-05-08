package com.senku.musicplayer.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PlayerManager(context: Context) {

    private val exoPlayer = ExoPlayer.Builder(context).build()

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun release() {
        exoPlayer.release()
    }
}
