package com.senku.musicplayer.player

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.senku.musicplayer.data.model.Song

object PlayerManager {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    var mediaController: MediaController? = null

    val currentSong = mutableStateOf<Song?>(null)
    val isPlaying = mutableStateOf(false)

    fun initialize(context: Context) {
        if (mediaController == null && controllerFuture == null) {
            val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
            controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture?.addListener({
                mediaController = controllerFuture?.get()
                mediaController?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlayingStatus: Boolean) {
                        isPlaying.value = isPlayingStatus
                    }
                })
            }, androidx.core.content.ContextCompat.getMainExecutor(context))
        }
    }

    fun playSong(context: Context, song: Song) {
        initialize(context)
        currentSong.value = song
        val mediaItem = MediaItem.fromUri(song.uri)

        // Ensure controller is ready
        if (mediaController != null) {
            mediaController?.setMediaItem(mediaItem)
            mediaController?.prepare()
            mediaController?.play()
        } else {
            // Wait for controller
            controllerFuture?.addListener({
                mediaController?.setMediaItem(mediaItem)
                mediaController?.prepare()
                mediaController?.play()
            }, androidx.core.content.ContextCompat.getMainExecutor(context))
        }
    }

    fun pause() {
        mediaController?.pause()
    }

    fun resume() {
        mediaController?.play()
    }

    fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
        mediaController = null
    }
}
