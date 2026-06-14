package ru.chivarzin.aleksandr.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.chivarzin.aleksandr.playlistmaker.data.dto.TrackDto
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class SearchHistoryDataSource(
    private val sharedPrefs: SharedPreferences,
) {

    companion object {
        private const val PREF_KEY = "search_history"
        private const val MAX_COUNT = 10
    }

    // Вспомогательный метод для получения и десериализации истории
    private fun getHistoryInternal(): ArrayList<TrackDto> {
        val json = sharedPrefs.getString(PREF_KEY, "[]")
        return Gson().fromJson(
            json,
            object : TypeToken<ArrayList<TrackDto>>() {}.type
        ) ?: ArrayList()
    }

    // Вспомогательный метод для сериализации и сохранения истории
    private fun saveHistoryInternal(history: List<TrackDto>) {
        val json = Gson().toJson(history)
        sharedPrefs.edit().putString(PREF_KEY, json).apply()
    }

    fun getHistory(): List<TrackDto> {
        return getHistoryInternal()
    }

    fun addToHistory(track: TrackDto) {
        val history = getHistoryInternal()

        // Добавляем новый элемент в начало списка
        history.add(0, track)

        // Удаляем дубликаты по trackId
        for (i in history.indices) {
            if (i == 0) {
                continue
            }
            if (history[i].trackId == track.trackId) {
                history.removeAt(i)
                break
            }
        }
        // Обрезаем список до максимального размера
        if (history.size > MAX_COUNT) {
            history.subList(MAX_COUNT, history.size).clear()
        }
        saveHistoryInternal(history)

    }

    fun clearHistory() {
        saveHistoryInternal(emptyList())
    }

    fun isEmpty(): Boolean {
        return getHistoryInternal().isEmpty()
    }
}

