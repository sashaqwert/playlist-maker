package ru.chivarzin.aleksandr.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistByID(id: Long): PlaylistEntity

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}