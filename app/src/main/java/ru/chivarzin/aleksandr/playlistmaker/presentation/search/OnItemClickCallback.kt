package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

fun interface OnItemClickCallback {
    fun callback(track: Track)
}