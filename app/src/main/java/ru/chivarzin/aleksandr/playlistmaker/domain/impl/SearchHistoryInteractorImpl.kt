package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import android.content.Context
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl (private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}