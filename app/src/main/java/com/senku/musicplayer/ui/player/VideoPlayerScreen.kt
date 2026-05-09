package com.senku.musicplayer.ui.player

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

@Composable
fun VideoPlayerScreen(uriString: String?) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val libVLC = remember {
        val options = ArrayList<String>()
        options.add("--no-drop-late-frames")
        options.add("--no-skip-frames")
        options.add("--rtsp-tcp")
        options.add("-vvv")
        LibVLC(context, options)
    }
    val mediaPlayer = remember { MediaPlayer(libVLC) }

    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    mediaPlayer.pause()
                    isPlaying = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (isPlaying) mediaPlayer.play()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.detachViews()
            mediaPlayer.release()
            libVLC.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        AndroidView(
            factory = { ctx ->
                VLCVideoLayout(ctx).apply {
                    mediaPlayer.attachViews(this, null, false, false)
                    if (uriString != null) {
                        val media = Media(libVLC, Uri.parse(uriString))
                        media.setHWDecoderEnabled(true, false)
                        mediaPlayer.media = media
                        media.release()
                        mediaPlayer.play()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (showControls) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            isPlaying = false
                        } else {
                            mediaPlayer.play()
                            isPlaying = true
                        }
                    },
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.padding(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
