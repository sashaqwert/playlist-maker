package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

sealed interface SearchState {
    object Loading : SearchState

    data class History (
        val tracks: List<Track>
    ) : SearchState

    object emptyHistory : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val message: String
    ) : SearchState

    data class Empty(
        val message: String
    ) : SearchState
}