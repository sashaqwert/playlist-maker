package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

sealed interface DialogState {
    object Loading: DialogState
    data class Content(val playlists: List<PlaylistPresentation>): DialogState
    data class Added(val playlist_name: String): DialogState
    data class AlreadyAdded(val playlist_name: String): DialogState
}