package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: TrackPresentation, val mediaPlayer: MediaPlayer, val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val uiStateLiveData = MutableLiveData<PlayerState>(PlayerState.Initial(track))
    fun observeUiState(): LiveData<PlayerState> = uiStateLiveData

    private var playerState = STATE_DEFAULT

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    fun onPlayButtonClicked() {
        when(playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    fun onFavoriteButtonClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.removeFromFavorite(toTrackDomain(track))
                track.isFavorite = false
            } else {
                favoriteInteractor.addToFavorite(toTrackDomain(track))
                track.isFavorite = true
            }
            uiStateLiveData.value = PlayerState.Initial(track)
        }
    }

    private fun toTrackDomain(track: TrackPresentation): Track {
        return Track(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl, track.isFavorite)
    }

    private fun preparePlayer() {
        if (track.previewUrl == null) {
            return
        }
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            uiStateLiveData.value = PlayerState.State(STATE_PREPARED)
            timerJob?.cancel()
            uiStateLiveData.value = PlayerState.Progress("00:00")
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        uiStateLiveData.value = PlayerState.State(STATE_PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState = STATE_PAUSED
        uiStateLiveData.value = PlayerState.State(STATE_PAUSED)
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                uiStateLiveData.value = PlayerState.Progress(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
            }
        }
    }

    fun onPause() {
        pausePlayer()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}