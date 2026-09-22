package ru.chivarzin.aleksandr.playlistmaker.data.converters

import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.PlaylistEntity
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(playlist.id, playlist.name, playlist.description, playlist.artwork_path,
            playlist.track_IDs, playlist.tracks_count)
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlist.id, playlist.name, playlist.description, playlist.artwork_path,
            playlist.track_IDs, playlist.tracks_count)
    }
}