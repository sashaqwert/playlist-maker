package ru.chivarzin.aleksandr.playlistmaker.domain.models

import android.os.Parcel
import android.os.Parcelable

data class Track (
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
) : Parcelable
{

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        // readValue возвращает Any?. Приводим к строке безопасно.
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String,
        if (parcel.readByte().toInt() == 0) null else parcel.readLong(),
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String,
        parcel.readValue(String::class.java.classLoader) as? String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(trackId)
        parcel.writeValue(trackName)
        parcel.writeValue(artistName)

        // Для Nullable Long используем байт-флаг (0 - null, 1 - значение)
        if (trackTimeMillis == null) {
            parcel.writeByte(0)
        } else {
            parcel.writeByte(1)
            parcel.writeLong(trackTimeMillis!!)
        }

        parcel.writeValue(artworkUrl100)
        parcel.writeValue(collectionName)
        parcel.writeValue(releaseDate)
        parcel.writeValue(primaryGenreName)
        parcel.writeValue(country)
        parcel.writeValue(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }

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