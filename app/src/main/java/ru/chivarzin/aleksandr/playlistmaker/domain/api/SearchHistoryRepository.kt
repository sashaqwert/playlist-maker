package ru.chivarzin.aleksandr.playlistmaker.domain.api

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    suspend fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
    fun isEmpty() : Boolean
}