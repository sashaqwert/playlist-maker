package ru.chivarzin.aleksandr.playlistmaker

data class Track (
    val trackId: Long,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в милисекундах
    val artworkUrl100: String? // Ссылка на изображение обложки
)
{

    fun getCoverArtwork() : String? {
        if (artworkUrl100 != null) {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }
        return null
    }

}