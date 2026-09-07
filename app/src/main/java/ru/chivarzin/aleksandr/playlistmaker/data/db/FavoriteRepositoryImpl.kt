package ru.chivarzin.aleksandr.playlistmaker.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.chivarzin.aleksandr.playlistmaker.data.converters.TrackDbConverter
import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.TrackEntity
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConverter
): FavoriteRepository {
    override suspend fun addToFavorite(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun removeFromFavorite(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getAllTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackDbConvertor.map(it) }
    }
}