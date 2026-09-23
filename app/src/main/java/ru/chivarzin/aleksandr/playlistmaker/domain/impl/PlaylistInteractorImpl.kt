package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.db.PlaylistRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getPlaylistByID(id: Long): Flow<Playlist> {
        return repository.getPlaylistByID(id)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }
}