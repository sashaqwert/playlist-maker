package ru.chivarzin.aleksandr.playlistmaker.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.PlaylistEntity
import ru.chivarzin.aleksandr.playlistmaker.data.dto.TrackDto
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class PlaylistDbConverter(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(playlist.id, playlist.name, playlist.description, playlist.artwork_path,
            gson.toJson(playlist.tracks), playlist.tracks_count)
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlist.id, playlist.name, playlist.description, playlist.artwork_path,
            gson.fromJson(
                playlist.tracks,
                object : TypeToken<List<Track>>() {}.type), playlist.tracks_count)
    }
}