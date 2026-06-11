package ru.chivarzin.aleksandr.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.chivarzin.aleksandr.playlistmaker.APP_PREFERENCES
import ru.chivarzin.aleksandr.playlistmaker.data.SearchHistoryDataSource
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl (val context: Context) : SearchHistoryRepository {

    val dataSource: SearchHistoryDataSource = SearchHistoryDataSource(context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE))
    override fun getHistory(): List<Track> {
        return dataSource.getHistory()
    }

    override fun addToHistory(track: Track) {
        dataSource.addToHistory(track)
    }

    override fun clearHistory() {
        dataSource.clearHistory()
    }

    override fun isEmpty(): Boolean {
        return dataSource.isEmpty()
    }
}