package ru.chivarzin.aleksandr.playlistmaker.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.chivarzin.aleksandr.playlistmaker.data.converters.PlaylistDbConverter
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase, val converter: PlaylistDbConverter): PlaylistRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(playlists.map { converter.map(it) })
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(converter.map(playlist))
    }

    override fun getPlaylistByID(id: Long): Flow<Playlist> = flow {
        emit(converter.map(appDatabase.playlistDao().getPlaylistByID(id)))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(converter.map(playlist))
    }
}