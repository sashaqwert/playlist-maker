package ru.chivarzin.aleksandr.playlistmaker.domain.api

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun findMusic(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}