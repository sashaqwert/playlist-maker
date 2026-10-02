package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class PlayerControlsBottomSheetViewModel(private val track: TrackPresentation, private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val uiStateLiveData = MutableLiveData<DialogState>()
    fun observeUiState(): LiveData<DialogState> = uiStateLiveData

    fun fillData() {
        uiStateLiveData.value = DialogState.Loading
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                uiStateLiveData.value = DialogState.Content(it)
            }
        }
    }

    fun add_track_to_playlist(playlist: Playlist) {
        val _is = playlist.add_track(track.toTrackDomain())
        if (_is) {
            viewModelScope.launch {
                playlistInteractor.addPlaylist(playlist)
                uiStateLiveData.value = DialogState.Added(playlist.name)
            }
        } else {
            uiStateLiveData.value = DialogState.AlreadyAdded(playlist.name)
        }
    }
}