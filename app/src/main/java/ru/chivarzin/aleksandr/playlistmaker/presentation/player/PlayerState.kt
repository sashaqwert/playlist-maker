package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

sealed interface PlayerState {
    data class Initial(val track: TrackPresentation) : PlayerState
    object StatePrepared: PlayerState
    object StatePlaying: PlayerState
    object StatePaused: PlayerState
    data class Progress(val progress: String) : PlayerState
}