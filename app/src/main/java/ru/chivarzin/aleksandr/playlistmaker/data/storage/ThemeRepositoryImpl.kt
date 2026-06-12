package ru.chivarzin.aleksandr.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.chivarzin.aleksandr.playlistmaker.APP_PREFERENCES
import ru.chivarzin.aleksandr.playlistmaker.data.ThemeDataSource
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl (val context: Context) : ThemeRepository {
    val dataSource : ThemeDataSource = ThemeDataSource(context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE))

    override fun saveTheme(isDarkTheme: Boolean) {
        dataSource.saveTheme(isDarkTheme)
    }

    override fun getTheme(): Boolean {
        return dataSource.getTheme()
    }
}