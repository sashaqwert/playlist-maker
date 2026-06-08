package ru.chivarzin.aleksandr.playlistmaker.data.dto

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

data class SearchResult (val resultCount: Int,
                         val results: List<Track>) : Response()