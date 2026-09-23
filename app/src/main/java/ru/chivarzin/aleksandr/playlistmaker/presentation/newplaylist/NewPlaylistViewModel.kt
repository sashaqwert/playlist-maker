package ru.chivarzin.aleksandr.playlistmaker.presentation.newplaylist

import androidx.lifecycle.ViewModel
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class NewPlaylistViewModel(private val track: TrackPresentation? = null, private val playlistInteractor: PlaylistInteractor): ViewModel() {
    fun createButtonClicked() {}
}