package ru.chivarzin.aleksandr.playlistmaker.presentation.newplaylist

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.api.FileInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

open class NewPlaylistViewModel(private var track: TrackPresentation? = null, private val playlistInteractor: PlaylistInteractor,
                                private val fileInteractor: FileInteractor): ViewModel() {
    private val saveMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    open fun obsorveSave(): LiveData<Boolean> = saveMutableLiveData

    fun createButtonClicked(playlist_name: String, playlist_description: String, artwork_name: String) {
        viewModelScope.launch {
            val pl = Playlist(name = playlist_name, description = playlist_description, artwork_path = artwork_name)
            if (track != null) {
                if (ADD_TRACK_TO_NEW_PLAYLIST) {
                    pl.add_track(track!!.toTrackDomain())
                }
            }
            playlistInteractor.addPlaylist(pl)
            saveMutableLiveData.value = true
        }
    }

    fun saveFile(sourceUri: Uri, filename_without_extension: String): Uri {
        return fileInteractor.saveFile(sourceUri.toString(), filename_without_extension).toUri()
    }

    fun set_track(track: TrackPresentation?) {
        this.track = track
    }

    companion object {
        private  const val ADD_TRACK_TO_NEW_PLAYLIST = false
    }
}