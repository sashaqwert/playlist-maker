package ru.chivarzin.aleksandr.playlistmaker.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.chivarzin.aleksandr.playlistmaker.data.NetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchRequest
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchResult
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class TracksRepositoryImpl (private val networkClient: NetworkClient, val favoriteRepository: FavoriteRepository) : TracksRepository {
    override fun findMusic(expression: String): Flow<List<Track>?> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            var ids = listOf<Long>()
            favoriteRepository.getFavoritesIDs().collect {
                ids = it
            }
            with(response as SearchResult) {
                emit(response.results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                        ids.contains(it.trackId)
                    )
                }
                )
            }
        } else {
            emit(null)
        }
    }
}