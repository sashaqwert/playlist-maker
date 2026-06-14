package ru.chivarzin.aleksandr.playlistmaker.data.dto

data class TrackDto(
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в милисекундах
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год трека
    val primaryGenreName: String?, // Жанр
    val country :String?, // Страна исполнителя
    val previewUrl: String? // 30-и секундный отрезок трека
)
