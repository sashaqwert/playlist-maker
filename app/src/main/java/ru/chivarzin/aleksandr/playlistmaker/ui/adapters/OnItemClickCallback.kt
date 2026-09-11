package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

fun interface OnItemClickCallback {
    fun callback(track: TrackPresentation)
}