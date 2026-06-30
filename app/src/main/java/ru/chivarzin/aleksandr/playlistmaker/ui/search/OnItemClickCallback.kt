package ru.chivarzin.aleksandr.playlistmaker.ui.search

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

fun interface OnItemClickCallback {
    fun callback(track: Track)
}