package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

fun interface OnPlaylistClickCallback {
    fun callback(playlist: PlaylistPresentation)
}