package ru.chivarzin.aleksandr.playlistmaker.domain.db

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface FavoriteInteractor {
    fun getFavorites(): Flow<List<Track>>
    suspend fun addToFavorite(track: Track)
    suspend fun removeFromFavorite(track: Track)
}