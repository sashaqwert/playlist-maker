package ru.chivarzin.aleksandr.playlistmaker.ui.search

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

fun interface OnItemClickCallback {
    fun callback(track: TrackPresentation)
}