package ru.chivarzin.aleksandr.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: TrackPresentation) : ViewModel() {

    private val uiStateLiveData = MutableLiveData<PlayerState>(PlayerState.Initial(track))
    fun observeUiState(): LiveData<PlayerState> = uiStateLiveData

    private var playerStateLiveData = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData == STATE_PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when(playerStateLiveData) {
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
            playerStateLiveData = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData = STATE_PREPARED
            uiStateLiveData.value = PlayerState.State(STATE_PREPARED)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData = STATE_PLAYING
        uiStateLiveData.value = PlayerState.State(STATE_PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData = STATE_PAUSED
        uiStateLiveData.value = PlayerState.State(STATE_PAUSED)
    }

    private fun startTimerUpdate() {
        uiStateLiveData.value = PlayerState.Progress(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
        handler.postDelayed(timerRunnable, 200)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        uiStateLiveData.value = PlayerState.Progress("00:00")
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