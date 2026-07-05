package ru.chivarzin.aleksandr.playlistmaker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.chivarzin.aleksandr.playlistmaker.creator.Creator
import ru.chivarzin.aleksandr.playlistmaker.data.storage.ThemeRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.di.dataModule
import ru.chivarzin.aleksandr.playlistmaker.di.interactorModule
import ru.chivarzin.aleksandr.playlistmaker.di.repositoryModule
import ru.chivarzin.aleksandr.playlistmaker.di.viewModelModule
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.ThemeInteractorImpl

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        switchTheme(ThemeInteractorImpl(ThemeRepositoryImpl(this)).getTheme())
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

// Source - https://stackoverflow.com/a/57686965
// Posted by Izadi Egizabal
// Retrieved 2026-04-09, License - CC BY-SA 4.0
fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}