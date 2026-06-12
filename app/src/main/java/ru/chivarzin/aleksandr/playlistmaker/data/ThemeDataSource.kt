package ru.chivarzin.aleksandr.playlistmaker.data

import android.content.SharedPreferences
import ru.chivarzin.aleksandr.playlistmaker.DARK_THEME_ENABLED
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository

class ThemeDataSource (val sharedPrefs: SharedPreferences) : ThemeRepository {
    override fun saveTheme(isDarkTheme: Boolean) {
        sharedPrefs.edit().putBoolean(DARK_THEME_ENABLED, isDarkTheme).apply()
    }

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_ENABLED, false)
    }
}