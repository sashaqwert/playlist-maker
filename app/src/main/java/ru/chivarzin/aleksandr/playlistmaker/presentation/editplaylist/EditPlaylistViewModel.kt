package ru.chivarzin.aleksandr.playlistmaker.presentation.editplaylist

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.api.FileInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.newplaylist.NewPlaylistViewModel

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor,
                            private val fileInteractor: FileInteractor): NewPlaylistViewModel(null, playlistInteractor, fileInteractor) {
    private val saveMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    override fun obsorveSave(): LiveData<Boolean> = saveMutableLiveData

    fun saveButtonClicked(playlist: PlaylistPresentation) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist.toPlaylistDomain())
            saveMutableLiveData.value = true
        }
    }
}