package ru.chivarzin.aleksandr.playlistmaker.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class PlaylistViewNodel(val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistPresentation>()
    fun observeState(): LiveData<PlaylistPresentation> = stateLiveData
    private var playlist: PlaylistPresentation? = null

    fun setPlaylist(playlist: PlaylistPresentation) {
        this.playlist = playlist
        stateLiveData.value = this.playlist!!
    }

    fun removeTrack(track: TrackPresentation) {
        playlist?.tracks?.removeIf { it.trackId == track.trackId }
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist!!.toPlaylistDomain())
            stateLiveData.value = playlist
        }
    }
}