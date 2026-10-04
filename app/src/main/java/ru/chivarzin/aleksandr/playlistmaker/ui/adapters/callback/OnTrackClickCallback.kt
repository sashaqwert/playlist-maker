package ru.chivarzin.aleksandr.playlistmaker.ui.adapters.callback

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

fun interface OnTrackClickCallback {
    fun callback(track: TrackPresentation)
}