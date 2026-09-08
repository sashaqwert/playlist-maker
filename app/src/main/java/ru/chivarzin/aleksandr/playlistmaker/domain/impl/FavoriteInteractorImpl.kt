package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class FavoriteInteractorImpl (private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun getFavorites(): Flow<List<Track>> {
        return favoriteRepository.getFavorites()
    }

    override suspend fun addToFavorite(track: Track) {
        favoriteRepository.addToFavorite(track)
    }

    override suspend fun removeFromFavorite(track: Track) {
        favoriteRepository.removeFromFavorite(track)
    }
}