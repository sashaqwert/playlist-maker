package ru.chivarzin.aleksandr.playlistmaker.domain.api

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}