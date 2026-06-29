package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.chivarzin.aleksandr.playlistmaker.App
import ru.chivarzin.aleksandr.playlistmaker.creator.Creator
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class SearchViewModel (val context: Context) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SearchViewModel(app)
            }
        }
    }

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(context)

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (latestSearchText == "" || latestSearchText == null) {
            if (searchHistoryInteractor.isEmpty()) {
                renderState(SearchState.emptyHistory)
            } else {
                renderState(SearchState.History(searchHistoryInteractor.getHistory()))
            }
        }

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            tracksInteractor.findMusic(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        val tracks = mutableListOf<Track>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                        }

                        when {
                            foundTracks == null -> {
                                renderState(
                                    SearchState.Error(context.getString(R.string.no_internet))
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    SearchState.Empty(context.getString(R.string.search_not_found))
                                )
                            }

                            else -> {
                                renderState(
                                    SearchState.Content(tracks)
                                )
                            }
                        }

                    }
                }
            })
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearHistory()
        renderState(SearchState.emptyHistory)
    }

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToHistory(track)
    }

    fun showSearchHistoryIfNotEmpty() {
        if (searchHistoryInteractor.isEmpty()) {
            renderState(SearchState.emptyHistory)
        } else {
            renderState(SearchState.History(searchHistoryInteractor.getHistory()))
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

}