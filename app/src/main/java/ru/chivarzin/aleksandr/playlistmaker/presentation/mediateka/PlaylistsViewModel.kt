package ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistsViewModel(val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun fillData() {
        stateLiveData.value = PlaylistsState.Loading
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) {
                   stateLiveData.value = PlaylistsState.Empty
                } else {
                    stateLiveData.value = PlaylistsState.Content(it)
                }
            }
        }
    }
}