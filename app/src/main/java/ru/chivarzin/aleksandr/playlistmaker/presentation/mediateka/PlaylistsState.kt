package ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

sealed interface PlaylistsState {
    object Loading: PlaylistsState
    data class Content(val playlists: List<PlaylistPresentation>): PlaylistsState
    object Empty: PlaylistsState

}