package ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

sealed interface PlaylistsState {
    object Loading: PlaylistsState
    data class Content(val playlists: List<Playlist>): PlaylistsState
    object Empty: PlaylistsState

}