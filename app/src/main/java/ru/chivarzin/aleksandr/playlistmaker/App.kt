package ru.chivarzin.aleksandr.playlistmaker

import android.app.Application
import android.content.Context
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_ENABLED, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

// GLOBAL FUNCTIONS

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}