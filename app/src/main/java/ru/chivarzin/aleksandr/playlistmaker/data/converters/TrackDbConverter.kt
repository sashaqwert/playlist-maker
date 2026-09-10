package ru.chivarzin.aleksandr.playlistmaker.data.converters

import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.TrackEntity
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class TrackDbConverter {
    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl, true)
    }
}