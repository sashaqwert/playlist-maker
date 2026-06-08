package ru.chivarzin.aleksandr.playlistmaker.domain.api

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface TracksRepository {
    fun findMusic(expression: String): List<Track>
}
