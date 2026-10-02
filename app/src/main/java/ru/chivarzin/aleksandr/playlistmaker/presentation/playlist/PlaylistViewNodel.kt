package ru.chivarzin.aleksandr.playlistmaker.presentation.playlist

import androidx.lifecycle.ViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

class PlaylistViewNodel: ViewModel() {
    private var playlist: PlaylistPresentation? = null

    fun setPlaylist(playlist: PlaylistPresentation) {
        this.playlist = playlist
    }
}