package com.senku.musicplayer.ui.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senku.musicplayer.data.model.Song
import com.senku.musicplayer.utils.MusicScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val songs = mutableStateListOf<Song>()

    var isLoading = true

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {

            val scanner = MusicScanner(getApplication())
            val musicList = scanner.getAllSongs()

            songs.clear()
            songs.addAll(musicList)

            isLoading = false
        }
    }
}
