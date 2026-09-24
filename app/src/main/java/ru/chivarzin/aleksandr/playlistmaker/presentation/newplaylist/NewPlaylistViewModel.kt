package ru.chivarzin.aleksandr.playlistmaker.presentation.newplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class NewPlaylistViewModel(private val track: TrackPresentation? = null, private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val saveMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    fun obsorveSave(): LiveData<Boolean> = saveMutableLiveData

    fun createButtonClicked(playlist_name: String, playlist_description: String, artwork_name: String) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(Playlist(name = playlist_name, description = playlist_description, artwork_path = artwork_name))
            saveMutableLiveData.value = true
        }
    }
}