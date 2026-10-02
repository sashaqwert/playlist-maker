package ru.chivarzin.aleksandr.playlistmaker.presentation.models

import android.os.Parcel
import android.os.Parcelable
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import kotlin.collections.mutableListOf

data class PlaylistPresentation(val id: Long = 0L,
                                val name: String,
                                val description: String,
                                val artwork_path: String,
                                val tracks: MutableList<TrackPresentation> = mutableListOf<TrackPresentation>(),
                                var tracks_count : Int = 0) : Parcelable
{
    constructor(playlist: Playlist): this(playlist.id,
        playlist.name,
        playlist.description,
        playlist.artwork_path,
        playlist.tracks.map { TrackPresentation(it) } as MutableList<TrackPresentation>,
        playlist.tracks_count)

    constructor(parcel: Parcel) : this( // https://giga.chat/link/gcsHhqwrRI
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(TrackPresentation.CREATOR)?.toMutableList() ?: mutableListOf(),
        parcel.readInt()
    )

    fun toPlaylistDomain(): Playlist {
        return Playlist(this.id,
            this.name,
            this.description,
            this.artwork_path,
            this.tracks.map { it.toTrackDomain() } as MutableList<Track>,
            this.tracks_count)
    }

    fun add_track(track: TrackPresentation): Boolean {
        if (track !in tracks) {
            tracks.add(track)
            tracks_count += 1
            return true
        }
        return false
    }

    // https://giga.chat/link/gcsHhqwrRI

    // Описание содержимого (битовые флаги). 0 означает, что файловых дескрипторов нет.
    override fun describeContents(): Int {
        return 0
    }

    // Метод записи данных в Parcel. Порядок важен!
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(artwork_path)
     // writeTypedList корректно обработает MutableList<TrackPresentation>
        parcel.writeTypedList(tracks)
        parcel.writeInt(tracks_count)
    }

    // Объект-компаньон для создания экземпляра из Parcel
    companion object CREATOR : Parcelable.Creator<PlaylistPresentation> {
        override fun createFromParcel(parcel: Parcel): PlaylistPresentation {
            return PlaylistPresentation(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistPresentation?> {
            return arrayOfNulls(size)
        }
    }
}