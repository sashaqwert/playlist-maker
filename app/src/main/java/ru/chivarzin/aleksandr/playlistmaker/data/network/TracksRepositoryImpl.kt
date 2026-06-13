package ru.chivarzin.aleksandr.playlistmaker.data.network

import ru.chivarzin.aleksandr.playlistmaker.data.NetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchRequest
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchResult
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun findMusic(expression: String): List<Track>? {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as SearchResult).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
                    it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
            }
        } else {
            return null
        }
    }
}