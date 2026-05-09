package com.senku.musicplayer.ui.player

import android.net.Uri
import android.view.ViewGroup
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

    var isPlaying by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }

    val libVLC = remember {
        LibVLC(
            context,
            arrayListOf(
                "--aout=opensles",
                "--audio-time-stretch",
                "--avcodec-skiploopfilter=0",
                "--avcodec-skip-frame=0",
                "--avcodec-skip-idct=0",
                "--network-caching=150",
                "--live-caching=150",
                "--no-drop-late-frames",
                "--no-skip-frames",
                "--rtsp-tcp"
            )
        )
    }

    val mediaPlayer = remember {
        MediaPlayer(libVLC)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (!mediaPlayer.isPlaying && isPlaying) {
                        mediaPlayer.play()
                    }
                }

                Lifecycle.Event.ON_PAUSE -> {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    }
                }

                Lifecycle.Event.ON_DESTROY -> {
                    mediaPlayer.stop()
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
            .clickable {
                showControls = !showControls
            }
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->

                VLCVideoLayout(ctx).apply {

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    mediaPlayer.attachViews(this, null, false, false)

                    if (!uriString.isNullOrEmpty()) {

                        val media = Media(libVLC, Uri.parse(uriString))

                        media.setHWDecoderEnabled(true, true)
                        media.addOption(":network-caching=150")
                        media.addOption(":clock-jitter=0")
                        media.addOption(":clock-synchro=0")

                        mediaPlayer.media = media
                        media.release()

                        mediaPlayer.play()
                        isPlaying = true
                    }
                }
            }
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
                        id = if (mediaPlayer.isPlaying) {
                            android.R.drawable.ic_media_pause
                        } else {
                            android.R.drawable.ic_media_play
                        }
                    ),
                    contentDescription = "Video Controls",
                    tint = Color.White,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}
