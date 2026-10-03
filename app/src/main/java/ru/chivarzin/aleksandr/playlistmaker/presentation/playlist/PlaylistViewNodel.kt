package ru.chivarzin.aleksandr.playlistmaker.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

class PlaylistViewNodel: ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistPresentation>()
    fun observeState(): LiveData<PlaylistPresentation> = stateLiveData
    private var playlist: PlaylistPresentation? = null

    fun setPlaylist(playlist: PlaylistPresentation) {
        this.playlist = playlist
        stateLiveData.value = this.playlist!!
    }
}