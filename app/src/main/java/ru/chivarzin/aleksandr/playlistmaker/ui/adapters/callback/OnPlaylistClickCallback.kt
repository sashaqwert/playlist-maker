package ru.chivarzin.aleksandr.playlistmaker.ui.adapters.callback

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

fun interface OnPlaylistClickCallback {
    fun callback(playlist: PlaylistPresentation)
}