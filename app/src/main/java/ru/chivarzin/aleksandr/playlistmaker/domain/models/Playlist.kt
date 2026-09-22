package ru.chivarzin.aleksandr.playlistmaker.domain.models

data class Playlist(val id: Long = 0L,
                    val name: String,
                    val description: String,
                    val artwork_path: String,
                    val track_IDs: String = "[]",
                    val tracks_count : Int = 0)
