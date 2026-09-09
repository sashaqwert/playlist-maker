package ru.chivarzin.aleksandr.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.chivarzin.aleksandr.playlistmaker.APP_PREFERENCES
import ru.chivarzin.aleksandr.playlistmaker.data.SearchHistoryDataSource
import ru.chivarzin.aleksandr.playlistmaker.data.dto.TrackDto
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl (val dataSource: SearchHistoryDataSource, val favoriteRepository: FavoriteRepository) : SearchHistoryRepository {

    override suspend fun getHistory(): List<Track> {
        var ids = listOf<Long>()
        favoriteRepository.getFavoritesIDs().collect {
            ids = it
        }
        val result: List<Track> = dataSource.getHistory().map {
            Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
                it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl,
                ids.contains(it.trackId))
        }
        return result
    }

    override fun addToHistory(track: Track) {
        dataSource.addToHistory(TrackDto(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl))
    }

    override fun clearHistory() {
        dataSource.clearHistory()
    }

    override fun isEmpty(): Boolean {
        return dataSource.isEmpty()
    }
}