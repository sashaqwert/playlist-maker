package ru.chivarzin.aleksandr.playlistmaker.domain.db

import kotlinx.coroutines.flow.Flow
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylistByID(id: Long) : Flow<Playlist>
    suspend fun deletePlaylist(playlist: Playlist)
}