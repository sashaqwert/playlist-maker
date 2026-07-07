package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

sealed interface PlayerState {
    data class Initial(val track: TrackPresentation) : PlayerState
    data class State(val player_state: Int)
    data class Progress(val progress: String) : PlayerState
}