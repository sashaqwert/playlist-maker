package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

sealed interface DialogState {
    object Loading: DialogState
    data class Content(val playlists: List<Playlist>): DialogState
}