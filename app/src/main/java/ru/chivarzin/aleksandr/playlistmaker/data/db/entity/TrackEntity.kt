package ru.chivarzin.aleksandr.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity (
    @PrimaryKey
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в милисекундах
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год трека
    val primaryGenreName: String?, // Жанр
    val country :String?, // Страна исполнителя
    val previewUrl: String?, // 30-и секундный отрезок трека

    @ColumnInfo(name = "created_at", defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis()
)
