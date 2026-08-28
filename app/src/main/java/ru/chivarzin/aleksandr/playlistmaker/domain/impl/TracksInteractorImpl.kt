package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TracksRepository) : TracksInteractor {

    override fun findMusic(expression: String): Flow<List<Track>?> {
        return repository.findMusic(expression)
    }
}