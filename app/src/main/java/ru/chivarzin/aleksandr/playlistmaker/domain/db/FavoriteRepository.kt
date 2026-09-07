package ru.chivarzin.aleksandr.playlistmaker.domain.db

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface FavoriteRepository {
    suspend fun addToFavorite(track: Track)
    suspend fun removeFromFavorite(track: Track)
    fun getFavorites(): Flow<List<Track>>
    fun getFavoritesIDs(): Flow<List<String>>
}