package com.senku.musicplayer.utils

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.senku.musicplayer.data.model.Song

class MusicScanner(private val context: Context) {

    fun getAllSongs(): List<Song> {

        val songs = mutableListOf<Song>()

        val audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val audioProjection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION
        )
        val audioSortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            audioCollection,
            audioProjection,
            null,
            null,
            audioSortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: "Unknown"
                val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                val album = cursor.getString(albumColumn) ?: "Unknown Album"
                val duration = cursor.getLong(durationColumn)

                val contentUri = ContentUris.withAppendedId(audioCollection, id)

                songs.add(
                    Song(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        uri = contentUri,
                        albumArt = null,
                        isVideo = false
                    )
                )
            }
        }

        val videoCollection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val videoProjection = mutableListOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            videoProjection.add(MediaStore.Video.Media.ARTIST)
            videoProjection.add(MediaStore.Video.Media.ALBUM)
        }

        val videoSortOrder = "${MediaStore.Video.Media.TITLE} ASC"

        context.contentResolver.query(
            videoCollection,
            videoProjection.toTypedArray(),
            null,
            null,
            videoSortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            var artistColumn = -1
            var albumColumn = -1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                artistColumn = cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)
                albumColumn = cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)
            }

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: "Unknown Video"
                val artist = if (artistColumn >= 0) cursor.getString(artistColumn) ?: "Video" else "Video"
                val album = if (albumColumn >= 0) cursor.getString(albumColumn) ?: "Video" else "Video"
                val duration = cursor.getLong(durationColumn)

                val contentUri = ContentUris.withAppendedId(videoCollection, id)

                songs.add(
                    Song(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        uri = contentUri,
                        albumArt = null,
                        isVideo = true
                    )
                )
            }
        }

        return songs.sortedBy { it.title.lowercase() }
    }
}
