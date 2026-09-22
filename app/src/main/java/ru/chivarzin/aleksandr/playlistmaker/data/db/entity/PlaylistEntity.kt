package ru.chivarzin.aleksandr.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlaylistEntity
    (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
    val artwork_path: String,
    val tracks: String = "[]",
    val tracks_count : Int = 0
    ) {
}