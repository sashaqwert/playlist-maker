package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class SearchViewModel (private val tracksInteractor: TracksInteractor, private val searchHistoryInteractor: SearchHistoryInteractor, val context: Context) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        if (latestSearchText == "" || latestSearchText == null) {
            if (searchHistoryInteractor.isEmpty()) {
                renderState(SearchState.emptyHistory)
            } else {
                renderState(SearchState.History(searchHistoryInteractor.getHistory().map {
                    TrackPresentation(it)
                }))
            }
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                tracksInteractor.findMusic(newSearchText).collect { foundTracks ->
                    val tracks = mutableListOf<TrackPresentation>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks.map {
                            TrackPresentation(it)
                        })
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
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearHistory()
        renderState(SearchState.emptyHistory)
    }

    fun addToHistory(track: TrackPresentation) {
        val trackDomain = Track(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.previewUrl)
        searchHistoryInteractor.addToHistory(trackDomain)
    }

    fun showSearchHistoryIfNotEmpty() {
        if (searchHistoryInteractor.isEmpty()) {
            renderState(SearchState.emptyHistory)
        } else {
            renderState(SearchState.History(searchHistoryInteractor.getHistory().map {
                TrackPresentation(it)
            }))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}