package com.senku.musicplayer.player

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.senku.musicplayer.data.model.Song

object PlayerManager {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    var mediaController: MediaController? = null

    var currentPlaylist: List<Song> = emptyList()
    var currentIndex: Int = -1

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
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            next()
                        }
                    }
                })
            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun playPlaylist(context: Context, songs: List<Song>, startIndex: Int = 0) {
        if (songs.isEmpty() || startIndex !in songs.indices) return
        currentPlaylist = songs
        currentIndex = startIndex
        playSong(context, songs[currentIndex])
    }

    fun playSong(context: Context, song: Song) {
        initialize(context)
        currentSong.value = song
        val mediaItem = MediaItem.fromUri(song.uri)

        if (mediaController != null) {
            mediaController?.setMediaItem(mediaItem)
            mediaController?.prepare()
            mediaController?.play()
        } else {
            controllerFuture?.addListener({
                mediaController?.setMediaItem(mediaItem)
                mediaController?.prepare()
                mediaController?.play()
            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun pause() {
        mediaController?.pause()
    }

    fun resume() {
        mediaController?.play()
    }

    fun next() {
        if (currentPlaylist.isEmpty()) return
        currentIndex = (currentIndex + 1) % currentPlaylist.size
        currentSong.value?.let {
            // In a real app we'd pass context properly, but for simplified next we assume initialized
            val mediaItem = MediaItem.fromUri(currentPlaylist[currentIndex].uri)
            mediaController?.setMediaItem(mediaItem)
            mediaController?.prepare()
            mediaController?.play()
            currentSong.value = currentPlaylist[currentIndex]
        }
    }

    fun previous() {
        if (currentPlaylist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) currentPlaylist.size - 1 else currentIndex - 1
        currentSong.value?.let {
            val mediaItem = MediaItem.fromUri(currentPlaylist[currentIndex].uri)
            mediaController?.setMediaItem(mediaItem)
            mediaController?.prepare()
            mediaController?.play()
            currentSong.value = currentPlaylist[currentIndex]
        }
    }

    fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
        mediaController = null
    }
}
