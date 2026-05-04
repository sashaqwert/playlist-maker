package ru.chivarzin.aleksandr.playlistmaker

data class Track (
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в милисекундах
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год трека
    val primaryGenreName: String?, // Жанр
    val country :String? // Страна исполнителя
)
{

    fun getCoverArtwork() : String? {
        if (artworkUrl100 != null) {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }
        return null
    }

    fun getYear() : Int? {
        if (releaseDate == null) {
            return null
        }
        return releaseDate.replaceAfter('-', "").replace("-", "").toInt()
    }

}