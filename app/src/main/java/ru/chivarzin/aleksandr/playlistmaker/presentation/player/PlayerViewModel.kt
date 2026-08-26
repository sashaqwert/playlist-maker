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
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: TrackPresentation, val mediaPlayer: MediaPlayer) : ViewModel() {

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