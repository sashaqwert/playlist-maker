package ru.chivarzin.aleksandr.playlistmaker.domain.api

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface TracksRepository {
    fun findMusic(expression: String): Flow<List<Track>?>
}
