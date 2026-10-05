package ru.chivarzin.aleksandr.playlistmaker.ui.playlist

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

interface PlaylistBottomSheetListener {
    fun onPlaylistAction(action: String, playlist: PlaylistPresentation?)
}