package ru.chivarzin.aleksandr.playlistmaker.domain.db

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

interface FavoriteRepository {
    fun addToFavorite(track: Track)
    fun removeFromFavorite(track: Track)
    fun getFavorites(): Flow<List<Track>>
}