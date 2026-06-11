package ru.chivarzin.aleksandr.playlistmaker.domain.api

import android.content.Context
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}