package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

fun interface OnPlaylistClickCallback {
    fun callback(playlist: Playlist)
}